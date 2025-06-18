package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.status.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageMissMatchException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final CacheManager cacheManager;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final MessageMapper messageMapper;
  private final ReadStatusRepository readStatusRepository;
  private final ApplicationEventPublisher eventPublisher;

  //텍스트만 있는 메세지
  @Override
  @Transactional
  public MessageDto create(CreateMessageDto createMessageDto) throws DiscodeitException {
    log.info("메세지 생성 시작: userId = {}, channelId = {}, messageContent = {}",
        createMessageDto.getAuthorId(), createMessageDto.getChannelId(),
        createMessageDto.content());

    if (createMessageDto.content() == null) {
      log.warn("메세지 생성 정보 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    UUID channelId = createMessageDto.getChannelId();
    UUID authorId = createMessageDto.getAuthorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    Message message = new Message(author, createMessageDto.content(), channel);
    Message saved = messageRepository.save(message);

    log.info("메세지 생성 완료: messageId = {}", saved.getId());

    log.info("메세지 생성 이후 알림 발송 시작: messageId = {}", saved.getId());

    List<ReadStatus> subscribers = readStatusRepository.findByChannelId(channelId);
    log.debug("해당 채널 구독자 수 = {}", subscribers.size());

    subscribers.stream()
        .filter(ReadStatus::isNotificationEnabled) // 구독한 사용자들
        .filter(readStatus -> !readStatus.getUser().getId().equals(authorId)) // 보낸사람 제외
        .forEach(readStatus -> {
          NewMessageNotificationEvent event = new NewMessageNotificationEvent(
              readStatus.getUser().getId(),
              createMessageDto.getChannelId(),
              (channel.getName() == null || channel.getName().isEmpty()) ? "개인 채널"
                  : channel.getName(),
              message.getContent().length() > 20 ? message.getContent().substring(0, 20) + "..."
                  : message.getContent()
          );
          eventPublisher.publishEvent(event);
          Objects.requireNonNull(cacheManager.getCache("userReadStatuses"))
              .evictIfPresent(readStatus.getUser().getId());
          Objects.requireNonNull(cacheManager.getCache("userChannels"))
              .evictIfPresent(readStatus.getUser().getId());
        });
    log.info("메세지 생성 이후 알림 생성 이벤트 호출 완료: messageId = {}", saved.getId());

    return messageMapper.toDto(saved);
  }

  //텍스트 + 파일 메세지
  @Override
  @Transactional
  public MessageDto create(CreateMessageDto createMessageDto, List<MultipartFile> files)
      throws DiscodeitException {
    MessageDto messageDto = create(createMessageDto);

    log.info("메세지 파일 첨부 시작: messageId = {}", messageDto.id());

    if (files == null || files.isEmpty()) {
      log.warn("첨부 파일 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    Message message = messageRepository.findById(messageDto.id())
        .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

    log.debug("첨부 파일과 메세지 연결 시작");
    files.forEach(file -> {
      try {
        BinaryContent binaryContent = new BinaryContent(file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(), BinaryContentUploadStatus.WAITING);

        binaryContent = binaryContentRepository.save(binaryContent);

        message.addFile(binaryContent);

        eventPublisher.publishEvent(new BinaryContentCreatedEvent(
            binaryContent.getId(),
            file.getBytes()
        ));
        log.debug("첨부 파일 저장 이벤트 발행 완료: attachmentId = {}", binaryContent.getId());
      } catch (IOException e) {
        throw new BinaryContentException(ErrorCode.FILE_NOT_SAVED);
      }
    });
    log.info("첨부 파일이 있는 메세지 생성 완료: messageId ={}", message.getId());

    messageRepository.save(message);

    return messageMapper.toDto(message);
  }


  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAll() {
    return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto findById(String messageId) {
    Message message = messageRepository.findById(UUID.fromString(messageId)).orElse(null);
    if (message == null) {
      throw new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    return messageMapper.toDto(message);
  }

  //특정 문자열이 내용에 포함되어 있는 메세지 찾기
  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAllContainsContent(String content) {
    return messageRepository.findByContentContains(content).stream().map(messageMapper::toDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAllByAuthorId(String authorId) {
    User author = userRepository.findById(UUID.fromString(authorId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    //todo - 고민: 메세지를 검색할때 유저 아이디가 없다고 에러를 출력해야할까?
    //그냥 검색값의 오류인지...
    //나중에 search 기능 만들때 더 고민해보고 수정하기

    return messageRepository.findByAuthorId(author.getId()).stream().map(messageMapper::toDto)
        .toList();
  }

  //todo - repository 에 날짜로 조회하는 기능 만들기
  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAllByCreatedAt(Instant createdAt) {
    return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals(createdAt))
        .map(messageMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelIdWithCursor(String channelId,
      Instant cursor,
      Pageable pageable) {

    if (channelId == null) {
      throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        pageable.getSort());

    List<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        UUID.fromString(channelId),
        Optional.ofNullable(cursor).orElse(Instant.now()),
        pageRequest);

    //다음 페이지 존재하는지 확인하기
    boolean hasNext = messages.size() > pageable.getPageSize();

    // 요청한 size보다 많은 결과가 있으면 마지막 항목을 제거해야함!
    if (hasNext) {
      messages = messages.subList(0, pageable.getPageSize());
    }

    // 결과가 없거나 다음 페이지가 없는 경우,
    Instant nextCursor = null;
    if (!messages.isEmpty()) {
      // 마지막 항목의 ID를 다음 커서로 설정하기
      nextCursor = messages.get(messages.size() - 1).getCreatedAt();
    }

    List<MessageDto> messageDtos = messages.stream()
        .map(messageMapper::toDto)
        .toList();

    Long total = messageRepository.countMessagesByChannelId(UUID.fromString(channelId));
    return pageResponseMapper.fromPage(
        messageDtos,
        hasNext,
        messageDtos.size(),
        nextCursor == null ? null : nextCursor.toString(),
        total
    );
  }

/*  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelIdWithPaging(String channelId,
      Pageable pageable) {

//    조회하는 User가 해당 channel에 속하는지 확인하기 위함
//    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
//        UUID.fromString(channelId), UUID.fromString(userId)).orElse(null);
//    if (readStatus == null) {
//      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
//    }

    if (channelId == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);

    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Slice<Message> messageSlice = messageRepository.findByChannelIdOrderByCreatedAtDesc(
        UUID.fromString(channelId), pageable);

    Slice<MessageDto> messageDtoSlice = messageSlice.map(messageMapper::toDto);

    return pageResponseMapper.fromSlice(messageDtoSlice);
  }*/

  @PreAuthorize("principal.userDto.id == @basicMessageService.findById(#messageId).author.id")
  @Override
  @Transactional
  public MessageDto updateMessage(String messageId, UpdateMessageDto updateMessageDto)
      throws DiscodeitException {
    log.info("메세지 정보 수정 시작: messageId = {}", messageId);

    Message message = messageRepository.findById(UUID.fromString(messageId))
        .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

    //todo 예외명 뭐로하지
    if (updateMessageDto.newContent().isEmpty()) {
      log.error("메세지 수정 정보 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }
    if (!message.getAuthor().getId().toString().equals(updateMessageDto.userId())) {
      log.error("작성자가 아닌 사용자가 수정 시도");
      throw new MessageMissMatchException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    log.debug("메세지 내용 수정: messageId ={}, oldContent = {}, newContent = {}", messageId,
        message.getContent(), updateMessageDto.newContent());

    message.setContent(updateMessageDto.newContent());
    message.setUpdatedAt(updateMessageDto.updatedAt());

    //todo - 메세지의 이미지를 삭제하거나 추가하는 기능
    //if(!updateMessageDto.binaryContentIds().isEmpty()) {
    //여기 수정해야겠다
    //이미지자체를 받아와서
    //이미있으면 그냥 넘어가고
    //아니라면 추가해야함
    //message.addImages(updateMessageDto.binaryContentIds());
    //}
    Message savedMessage = messageRepository.save(message);
    log.info("메세지 수정 완료: messageId ={}, newContent = {}", savedMessage.getId(),
        message.getContent());

    return messageMapper.toDto(message);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == @basicMessageService.findById(#messageId).author.id")
  @Override
  @Transactional
  public boolean delete(String messageId, String userId) throws DiscodeitException {
    log.info("메세지 삭제 시작: messageId = {}, userId = {}", messageId, userId);
    Message message = messageRepository.findById(UUID.fromString(messageId))
        .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

    if (!message.getAuthor().getId().toString().equals(userId)) {
      log.error("작성자가 아닌 사용자가 삭제 시도: messageId ={}, authorId = {}, userId ={} ", messageId,
          message.getAuthor().getId(), userId);
      throw new DiscodeitException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    messageRepository.delete(message);
    log.info("메세지 삭제 완료: messageId ={}", messageId);

    return true;
  }


}
