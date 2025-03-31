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
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    log.debug("메시지 생성 서비스 진입 - channelId: {}, authorId: {}, 첨부파일 수: {}",
        channelId, authorId, binaryContentCreateRequests.size());

    Channel channel = channelRepository.findById(channelId)
        .orElseGet(
            () -> {
              log.warn("메시지 생성 실패 - 존재하지 않는 채널 - channelId: {}", channelId);
              throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
            });
    User author = userRepository.findById(authorId)
        .orElseGet(
            () -> {
              log.warn("메시지 생성 실패 - 존재하지 않는 사용자 - authorId: {}", authorId);
              throw new NoSuchElementException("Author with id " + authorId + " does not exist");
            });

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          try {
            String fileName = attachmentRequest.fileName();
            String contentType = attachmentRequest.contentType();
            byte[] bytes = attachmentRequest.bytes();

            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                contentType);
            binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(binaryContent.getId(), bytes);
            log.debug("첨부파일 저장 완료 - fileName: {}, contentType: {}, binaryContentId: {}",
                fileName, contentType, binaryContent.getId());

            return binaryContent;
          } catch (Exception e) {
            log.error("첨부파일 저장 실패 - fileName: {}", attachmentRequest.fileName(), e);
            throw e;
          }
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
    log.info("메시지 생성 완료 - messageId: {}, channelId: {}, authorId: {}, 첨부파일 수: {}",
        message.getId(), channelId, authorId, attachments.size());
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
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
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 업데이트 서비스 진입 - messageId: {}", messageId);
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseGet(
            () -> {
              log.warn("메세지를 찾을 수 없음 - id{}", messageId);
              throw new NoSuchElementException("Message with id " + messageId + " not found");
            });
    message.update(newContent);
    log.info("메시지 업데이트 성공 - messageId: {}", messageId);
    return messageMapper.toDto(message);
  }

  @Transactional
  public void delete(UUID messageId) {
    log.debug("메시지 삭제 서비스 진입 - messageId: {}", messageId);
    if (!messageRepository.existsById(messageId)) {
      log.warn("메세지를 찾을 수 없음 - id{}", messageId);
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }

    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료 - messageId: {}", messageId);
  }
}
