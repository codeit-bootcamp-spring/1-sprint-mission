package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    log.info("Message 생성 시작 - channelId:{} , authorId:{}", channelId, authorId);

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("Channel ID({}) 를 찾을 수 없음", channelId);
              return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                  Map.of("channelId", channelId));
            });

    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> {
              log.error("User ID({}) 를 찾을 수 없음", authorId);
              return new UserNotFoundExeption(ErrorCode.USER_NOT_FOUND, Map.of("userId", authorId));
            }
        );

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String filename = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(filename, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Binary content 저장 성공: {}", binaryContent);
          return binaryContent;
        })
        .toList();

    Message message = new Message(
        messageCreateRequest.content(),
        channel,
        author,
        attachments
    );
    messageRepository.save(message);
    log.info("Message 저장 성공: {}", message);
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(
            channelId, Optional.ofNullable(createdAt).orElse(Instant.now()), pageable)
        .map(messageMapper::toDto);
    Instant nexCursor = null;
    if (!slice.getContent().isEmpty()) {
      nexCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nexCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest updateMessageDto) {
    log.info("Message 업데이트 시작: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("Message ID({}) 를 찾을 수 없음", messageId);
              return new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
                  Map.of("messageId", messageId));
            });
    message.updateContent(updateMessageDto.newContent());

    log.info("Message 업데이트 성공: {}", messageId);
    return messageMapper.toDto(message);
  }


  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.info("Message 삭제 시작: {}", messageId);
    if (!messageRepository.existsById(messageId)) {
      log.error("Message ID({}) 를 찾을 수 없음", messageId);
      throw new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
          Map.of("messageId", messageId));
    }
    messageRepository.deleteById(messageId);
    log.info("Message 삭제 성공: {}", messageId);
  }
}
