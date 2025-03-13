package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.awt.print.Pageable;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

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
  public MessageDto create(CreateMessageDto createMessageDto) throws CustomException {
    if (createMessageDto.content() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        createMessageDto.getChannelId(),
        createMessageDto.getAuthorId()).orElse(null);

    if (readStatus == null) {
      //해당 채널에 참여하지 않은 사용자가 해당 private 채널이 존재한다는 사실도 몰라야 한다.
      //그래서 user not in channel 이 아닌 Channel not found 로 예외처리
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Message message = new Message(readStatus.getUser(), createMessageDto.content(),
        readStatus.getChannel());
    Message saved = messageRepository.save(message);

    return messageMapper.toDto(saved);
  }

  //텍스트 + 파일 메세지
  @Override
  @Transactional
  public MessageDto create(CreateMessageDto createMessageDto, List<MultipartFile> files)
      throws CustomException {
    MessageDto messageDto = create(createMessageDto);

    if (files == null || files.isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }

    Message message = messageRepository.findById(messageDto.id()).orElse(null);

    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }

    //더 좋은 방법이 없을까... 이건 Service를 의존해서 create를 쓰는게 좋을까?
    for (MultipartFile file : files) {
      BinaryContent savedContent = null;
      try {
        BinaryContent binaryContent = new BinaryContent(
            file.getName(),
            file.getContentType(),
            file.getSize()
        );
        savedContent = binaryContentRepository.save(binaryContent);
      } catch (RuntimeException e) {
        throw new RuntimeException(e);
      }
      message.addFile(savedContent);
    }

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
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
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
    User author = userRepository.findById(UUID.fromString(authorId)).orElse(null);

    if (author == null) {
      //todo - 고민: 메세지를 검색할때 유저 아이디가 없다고 에러를 출력해야할까?
      //그냥 검색값의 오류인지...
      //나중에 search 기능 만들때 더 고민해보고 수정하기
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
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
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

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

    List<MessageDto> messageDtos = messages.stream()
        .map(messageMapper::toDto)
        .toList();

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
      throws CustomException {
    Message message = messageRepository.findById(UUID.fromString(messageId)).orElse(null);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    if (updateMessageDto.newContent().isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }
    if (!message.getAuthor().getId().toString().equals(updateMessageDto.userId())) {
      throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

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

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public boolean delete(String messageId, String userId) throws CustomException {
    Message message = messageRepository.findById(UUID.fromString(messageId)).orElse(null);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    if (!message.getAuthor().getId().toString().equals(userId)) {
      throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    messageRepository.delete(message);

    return true;
  }
}
