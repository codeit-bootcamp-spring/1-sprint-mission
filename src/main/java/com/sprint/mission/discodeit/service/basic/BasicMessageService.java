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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));
    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> new NoSuchElementException("Author with id " + authorId + " does not exist"));

    String content = messageCreateRequest.content();
    Message message = new Message(content, channel, author);

    for (BinaryContentCreateRequest attachmentRequest : binaryContentCreateRequests) {
      BinaryContent binaryContent = new BinaryContent(
          attachmentRequest.fileName(),
          (long) attachmentRequest.bytes().length,
          attachmentRequest.contentType()
      );
      BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);

      binaryContentStorage.put(createdBinaryContent.getId(), attachmentRequest.bytes());
      message.addAttachment(createdBinaryContent);
    }

    return messageRepository.save(message);
  }

  @Transactional(readOnly = true)
  public Message findWithAllRelationships(UUID messageId) {
    return messageRepository.findWithAllRelationships(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }


  @Transactional(readOnly = true)
  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Transactional(readOnly = true)
  @Override
  public Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable) {
    return messageRepository.findByChannel_IdOrderByCreatedAtDesc(channelId, pageable);
  }

  @Transactional
  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    messageRepository.delete(message);
  }


  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findMessageDtosByChannelId(UUID channelId, Pageable pageable) {
    // 1. 페이징된 기본 메시지 데이터 조회 (작성자 정보 포함)
    Slice<Message> messageSlice = messageRepository.findByChannelIdPaged(channelId, pageable);

    // 2. 조회된 메시지의 ID 목록 추출
    List<UUID> messageIds = messageSlice.getContent().stream()
        .map(Message::getId)
        .collect(Collectors.toList());

    if (!messageIds.isEmpty()) {
      // 3. ID 목록으로 메시지와 모든 연관관계를 한 번에 조회
      List<Message> messagesWithRelationships =
          messageRepository.findMessagesWithAllRelationships(messageIds);

      // 4. ID를 기준으로 원래 정렬 순서 유지하면서 매핑
      List<MessageDto> messageDtos = messageIds.stream()
          .map(id -> messagesWithRelationships.stream()
              .filter(m -> m.getId().equals(id))
              .findFirst()
              .orElse(null))
          .filter(m -> m != null)
          .map(messageMapper::toDto)
          .collect(Collectors.toList());

      // 5. 동일한 페이징 정보 유지하면서 새로운 Slice 생성
      Slice<MessageDto> dtoSlice = new SliceImpl<>(
          messageDtos,
          messageSlice.getPageable(),
          messageSlice.hasNext()
      );

      return pageResponseMapper.fromSlice(dtoSlice);
    }
    // 메시지가 없는 경우 빈 결과 반환
    return pageResponseMapper.fromSlice(
        new SliceImpl<>(List.of(), pageable, false)
    );
  }


  /**
   * 커서 기반 페이지네이션을 사용하여 채널의 메시지를 조회합니다.
   *
   * @param channelId 채널 ID
   * @param cursor    이전 페이지의 마지막 메시지 생성 시간 (null이면 첫 페이지)
   * @param pageable  페이지 정보 (크기, 정렬)
   * @return 메시지 DTO 목록과 다음 페이지 정보가 담긴 PageResponse
   */
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findMessageDtosByChannelIdWithCursor(
      UUID channelId,
      Instant cursor,
      Pageable pageable
  ) {
    // 커서 기반 조회
    boolean cursorIsNull = (cursor == null);
    List<Message> messages = messageRepository.findByChannelIdAndCursorPaged(
        channelId,
        cursor != null ? cursor : Instant.now(),
        cursorIsNull,
        pageable
    );
    if (messages.isEmpty()) {
      // 결과가 없으면 빈 응답 반환
      return pageResponseMapper.fromCursorResult(
          List.of(),
          null,
          pageable.getPageSize(),
          false
      );
    }

    // 2. 조회된 메시지의 ID 목록 추출
    List<UUID> messageIds = messages.stream()
        .map(Message::getId)
        .collect(Collectors.toList());

    // 3. ID 목록으로 메시지와 모든 연관관계를 한 번에 조회
    List<Message> messagesWithRelationships =
        messageRepository.findMessagesWithAllRelationships(messageIds);

    // 4. DTO로 변환
    List<MessageDto> messageDtos = messageIds.stream()
        .map(id -> messagesWithRelationships.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null))
        .filter(m -> m != null)
        .map(messageMapper::toDto)
        .collect(Collectors.toList());

    // 5. 다음 페이지 존재 여부 확인
    Instant lastCreatedAt = messages.get(messages.size() - 1).getCreatedAt();
    boolean hasNext = messageRepository.existsByChannelIdAndCreatedAtLessThan(
        channelId,
        lastCreatedAt
    );

    // 6. 다음 커서 값 설정 (다음 페이지가 있으면 마지막 메시지의 생성시간)
    Instant nextCursor = hasNext ? lastCreatedAt : null;

    // 7. PageResponse 생성 및 반환
    return pageResponseMapper.fromCursorResult(
        messageDtos,
        nextCursor,
        pageable.getPageSize(),
        hasNext
    );
  }


}
