package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final MessageMapper messageMapper;

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

    log.debug("사용자가 채널에 속해있는지 ReadStatus로 검증 시작: channelId = {}, userId = {}",
        createMessageDto.getChannelId(), createMessageDto.getAuthorId());

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
            createMessageDto.getChannelId(), createMessageDto.getAuthorId())
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    //해당 채널에 참여하지 않은 사용자가 해당 private 채널이 존재한다는 사실도 몰라야 한다.
    //그래서 user not in channel 이 아닌 Channel not found 로 예외처리

    log.debug("사용자가 채널에 속해있는지 ReadStatus로 검증 완료: readStatusId = {}, channelId = {}, userId = {}",
        readStatus.getId(), readStatus.getChannel().getId(), readStatus.getUser().getId());

    Message message = new Message(readStatus.getUser(), createMessageDto.content(),
        readStatus.getChannel());
    Message saved = messageRepository.save(message);

    log.info("메세지 생성 완료: messageId = {}", saved.getId());

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

    //더 좋은 방법이 없을까... 이건 Service를 의존해서 create를 쓰는게 좋을까?
    log.debug("첨부 파일과 메세지 연결 시작");
    for (MultipartFile file : files) {
      BinaryContent savedContent = null;
      try {
        BinaryContent binaryContent = new BinaryContent(file.getName(), file.getContentType(),
            file.getSize());
        savedContent = binaryContentRepository.save(binaryContent);
        log.debug("첨부 파일 저장 완료: attachmentId = {}", savedContent.getId());
      } catch (RuntimeException e) {
        log.error("첨부 파일 저장 중 오류 발생: {}", e.getMessage());
        throw new RuntimeException(e);
      }
      message.addFile(savedContent);
      log.debug("첨부 파일과 메세지 연결 완료: messageId ={}, attachmentId = {}", message.getId(),
          savedContent.getId());
    }

    Message createdMessage = messageRepository.save(message);
    log.info("첨부 파일이 있는 메세지 생성 완료: messageId ={}", createdMessage.getId());

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
  public PageResponse<MessageDto> findAllByChannelIdWithCursor(String channelId, Instant cursor,
      int size) {

    if (channelId == null) {
      throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    Pageable pageable = (Pageable) PageRequest.of(0, size + 1);

    List<Message> messages = messageRepository.findByChannelIdAndCreatedAtBeforeCursorOrderByCreatedAtDesc(
        UUID.fromString(channelId), cursor, pageable);

    boolean hasNext = false;
    Instant nextCursorInstant = null;

    // 요청한 크기보다 많은 결과가 있으면 다음 페이지가 있다는 의미
    if (messages.size() > size) {
      hasNext = true;
      messages = messages.subList(0, size); // 마지막 항목은 제외
    }
    // 다음 커서 값 설정 (마지막 메시지의 createdAt)
    if (!messages.isEmpty() && hasNext) {
      nextCursorInstant = messages.get(messages.size() - 1).getCreatedAt();
    }

    List<MessageDto> messageDtos = messages.stream().map(messageMapper::toDto).toList();

    return pageResponseMapper.fromCursorResult(messageDtos, hasNext, size, nextCursorInstant, null);
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
