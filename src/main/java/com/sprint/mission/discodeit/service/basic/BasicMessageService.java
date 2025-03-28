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
import com.sprint.mission.discodeit.service.MessageService;
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

@Slf4j
@RequiredArgsConstructor
@Service
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
    log.info("[MessageService] 메시지 생성 시작 targetChannelId: {}, attachments: {}",
        messageCreateRequest.channelId(),
        (binaryContentCreateRequests.isEmpty() ? "첨부파일 없음" : "첨부파일 있음")
    );
    log.debug(
        "[MessageService] 메시지 생성 요청 정보 content: {}, channelId: {}, authorId: {}, attachments: {}",
        messageCreateRequest.content(),
        messageCreateRequest.channelId(),
        messageCreateRequest.authorId(),
        (binaryContentCreateRequests.isEmpty() ? "첨부파일 없음" : "첨부파일 있음")
    );
    if (!binaryContentCreateRequests.isEmpty()) {
      for (int i = 0; i < binaryContentCreateRequests.size(); i++) {
        BinaryContentCreateRequest request = binaryContentCreateRequests.get(i);
        log.debug("[MessageService] 메시지 첨부파일[{}] 정보 fileName: {}, contentType: {}, size: {}byte ",
            i, request.fileName(),
            request.contentType(),
            request.bytes().length
        );
      }
    }
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));
    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> new NoSuchElementException("Author with id " + authorId + " does not exist")
        );
    log.info("[MessageService] 메시지 첨부파일 저장작업 시작");
    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          log.info("[MessageService] 메시지 점부파일 저장 성공");
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
    log.info("[MessageService] 메시지 생성 성공 id: {}", message.getId());
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
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
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.info("[MessageService] 메시지 수정 시작 id: {}", messageId);
    log.debug("[MessageService] 메시지 수정 요청 정보 newContent: {}", request.newContent());
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    log.info("[MessageService] 메시지 수정 성공 id: {}", messageId);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.info("[MessageService] 메시지 삭제 시작 id: {}", messageId);
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }

    messageRepository.deleteById(messageId);
    log.info("[MessageService] 메시지 삭제 성공 id: {}", messageId);
  }
}
