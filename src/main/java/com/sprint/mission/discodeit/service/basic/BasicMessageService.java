package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.info("메시지 생성 요청 - channelId: {}, userId: {}", messageCreateRequest.channelId(), messageCreateRequest.authorId());
    
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.error("메시지 생성 실패 - 존재하지 않는 channelId: {}", channelId);
          return new ChannelException.ChannelNotFoundException("Channel with id " + channelId + " not found");
        });
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> {
          log.error("메시지 생성 실패 - 존재하지 않는 userId: {}", authorId);
          return new UserException.UserNotFoundException("User with id " + authorId + " not found");
        });

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );

    messageRepository.save(message);
    
    log.info("메시지 생성 완료 - messageId: {}, channelId: {}, userId: {}", 
        message.getId(), channel.getId(), author.getId());
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    log.debug("메시지 조회 - messageId: {}", messageId);
    
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> {
          log.error("메시지 조회 실패 - 존재하지 않는 messageId: {}", messageId);
          return new MessageException.MessageNotFoundException("Message with id " + messageId + " not found");
        });
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    log.debug("채널의 메시지 목록 조회 - channelId: {}, createAt: {}, pageable: {}", channelId, createAt, pageable);
    
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.info("메시지 수정 요청 - messageId: {}", messageId);
    
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.error("메시지 수정 실패 - 존재하지 않는 messageId: {}", messageId);
          return new MessageException.MessageNotFoundException("Message with id " + messageId + " not found");
        });
    message.update(newContent);
    
    log.info("메시지 수정 완료 - messageId: {}", messageId);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.info("메시지 삭제 요청 - messageId: {}", messageId);
    
    if (!messageRepository.existsById(messageId)) {
      log.error("메시지 삭제 실패 - 존재하지 않는 messageId: {}", messageId);
      throw new MessageException.MessageNotFoundException("Message with id " + messageId + " not found");
    }

    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료 - messageId: {}", messageId);
  }
}
