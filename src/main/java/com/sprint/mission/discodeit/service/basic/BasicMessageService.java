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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
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
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("[채널 조회 실패] 해당 채널을 찾을 수 없습니다. 채널 ID: {}", channelId);
              return new ChannelNotFoundException();
            });

    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> {
              log.error("[유저 조회 실패] 해당 유저를 찾을 수 없습니다. 유저 ID: {}", authorId);
              return new UserNotFoundException();
            }
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
    log.info("[메세지 첨부파일 생성 성공] 첨부파일 개수: {}", attachments.size());

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );
    log.info("[메세지 생성 시도] 메세지 ID: {}", message.getId());

    messageRepository.save(message);
    log.info("[메세지 생성 성공] 메세지 ID: {}", message.getId());

    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    log.info("[메세지 조회 시도] 메세지 ID: {}", messageId);

    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(
            () -> {
              log.error("[메세지 조회 실패] 해당하는 메세지가 존재하지 않습니다. 메세지 ID: {}", messageId);
              return new MessageNotFoundException();
            });
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    log.info("[채널의 메세지 조회 시도] 채널 ID: {}", channelId);

    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    log.info("[채널의 메세지 조회 성공] 채널 ID: {}", channelId);
    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    log.info("[메세지 수정 시도] 메세지 ID: {}", messageId);

    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("[메세지 조회 시도] 메세지 ID: {}", messageId);
              return new MessageNotFoundException();
            });

    message.update(newContent);
    log.info("[메세지 수정 성공] 메세지 ID: {}", message.getId());

    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.info("[메세지 삭제 시도] 메세지 ID: {}", messageId);

    if (!messageRepository.existsById(messageId)) {
      log.error("[메세지 조회 실패] 해당 메세지를 찾을 수 없습니다. 메세지 ID: {}", messageId);
      throw new MessageNotFoundException();
    }

    log.info("[메세지 삭제 성공] 메세지 ID: {}", messageId);
    messageRepository.deleteById(messageId);
  }
}
