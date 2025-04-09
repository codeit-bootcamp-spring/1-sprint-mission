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
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
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
    log.info("메시지 생성 요청: channelId={}, authorId={}, content={}",
            messageCreateRequest.channelId(), messageCreateRequest.authorId(), messageCreateRequest.content());

    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                    () -> new ChannelNotFoundException(channelId));
    User author = userRepository.findById(authorId)
            .orElseThrow(
                    () -> new UserNotFoundException(authorId)
            );

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
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
            .map(messageMapper::toDto)
        .orElseThrow(
            () -> new MessageNotFoundException(messageId));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
                                                  Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
            .map(messageMapper::toDto);

    Instant nextCursor = null;
    if(!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
              .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 수정 요청: messageId={}, newContent={}", messageId, request.newContent());

    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException(messageId));
      message.update(newContent);
      return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.warn("메시지 삭제 요청: messageId={}", messageId);

    if(!messageRepository.existsById(messageId)) {
      throw new MessageNotFoundException(messageId);
    }

    messageRepository.deleteById(messageId);
  }
}
