package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  // todo - 고민
  //userService를 의존하는게 맞는가?
  //userRepository를 의존하는게 맞는가?

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  //텍스트만 있는 메세지
  @Override
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

    return MessageDto.from(saved);
  }

  //텍스트 + 파일 메세지
  @Override
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
            file.getBytes(),
            file.getContentType(),
            file.getSize()
        );
        savedContent = binaryContentRepository.save(binaryContent);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      message.addFile(savedContent);
    }

    messageRepository.save(message);

    return MessageDto.from(message);
  }

  @Override
  public List<MessageDto> findAll() {
    return messageRepository.findAll().stream().map(MessageDto::from).toList();
  }

  @Override
  public MessageDto findById(String messageId) {
    Message message = messageRepository.findById(UUID.fromString(messageId)).orElse(null);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    return MessageDto.from(message);
  }

  //특정 문자열이 내용에 포함되어 있는 메세지 찾기
  @Override
  public List<MessageDto> findAllContainsContent(String content) {
    return messageRepository.findByContentContains(content).stream().map(MessageDto::from).toList();
  }

  @Override
  public List<MessageDto> findAllByAuthorId(String authorId) {
    User author = userRepository.findById(UUID.fromString(authorId)).orElse(null);

    if (author == null) {
      //todo - 고민: 메세지를 검색할때 유저 아이디가 없다고 에러를 출력해야할까?
      //그냥 검색값의 오류인지...
      //나중에 search 기능 만들때 더 고민해보고 수정하기
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    return messageRepository.findByAuthorId(authorId).stream().map(MessageDto::from).toList();
  }

  //todo - repository 에 날짜로 조회하는 기능 만들기
  @Override
  public List<MessageDto> findAllByCreatedAt(Instant createdAt) {
    return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals(createdAt))
        .map(MessageDto::from).toList();
  }

  @Override
  public List<MessageDto> findAllByChannelId(String channelId, String userId) {

    //해당 유저가 지금 채널에 속한 사람인지 확인하기
    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        UUID.fromString(channelId), UUID.fromString(userId)).orElse(null);

    if (readStatus == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    return messageRepository.findByChannelId(channelId).stream().map(MessageDto::from).toList();
  }

  @Override
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

    if (!message.getContent().equals(updateMessageDto.newContent())) {
      message.setContent(updateMessageDto.newContent());
      message.setUpdatedAt(updateMessageDto.updatedAt());
    }
    //todo - 메세지의 이미지를 삭제하거나 추가하는 기능
    //if(!updateMessageDto.binaryContentIds().isEmpty()) {
    //여기 수정해야겠다
    //이미지자체를 받아와서
    //이미있으면 그냥 넘어가고
    //아니라면 추가해야함
    //message.addImages(updateMessageDto.binaryContentIds());
    //}

    return MessageDto.from(messageRepository.save(message));
  }

  @Override
  public boolean delete(String messageId, String userId) throws CustomException {
    Message message = messageRepository.findById(UUID.fromString(messageId)).orElse(null);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    if (!message.getAuthor().getId().toString().equals(userId)) {
      throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    //todo - 고민: 이거는 cascade 옵션으로 같이 없앨 수 있을 것 같다.
    for (BinaryContent content : message.getAttachments()) {
      binaryContentRepository.delete(content);
    }
    messageRepository.delete(message);

    return true;
  }
}
