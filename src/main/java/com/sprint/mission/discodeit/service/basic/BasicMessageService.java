package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserService userService;
  private final ChannelService channelService;
  private final BinaryContentService binaryContentService;

  @Override
  public MessageDto create(CreateMessageDto createMessageDto) throws CustomException {
    if (createMessageDto.content() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }

    UserDto user = userService.findById(createMessageDto.authorId());
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    ChannelDto channel = channelService.findById(createMessageDto.channelId(), user.id());
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    if (channel.type() == ChannelType.PRIVATE && !channelService.isUserInChannel(channel.id(),
        user.id())) {
      System.out.println("User with id " + user.id() + " not found in this channel.");
      throw new CustomException(ErrorCode.USER_NOT_IN_CHANNEL);
    }

    Message message = new Message(createMessageDto.authorId(), createMessageDto.content(),
        createMessageDto.channelId());
    Message saved = messageRepository.save(message);

    return MessageDto.from(saved);

  }

  @Override
  public MessageDto create(CreateMessageDto createMessageDto, List<MultipartFile> files)
      throws CustomException {
    MessageDto messageDto = create(createMessageDto);

    if (files == null || files.isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }

    List<String> binaryContentIds = new ArrayList<>();
    for (MultipartFile file : files) {
      BinaryContentDto binaryContentDto = binaryContentService.create(file);
      binaryContentIds.add(binaryContentDto.id());
    }

    Message message = messageRepository.findById(messageDto.id());

    message.getAttachmentImageIds().addAll(binaryContentIds);

    messageRepository.save(message);

    return MessageDto.from(message);
  }

  @Override
  public List<MessageDto> findAll() {
    return messageRepository.findAll().stream().map(MessageDto::from).toList();
  }

  @Override
  public MessageDto findById(String messageId) {
    return MessageDto.from(messageRepository.findById(messageId));
  }

  @Override
  public List<MessageDto> findAllContainsContent(String content) {
    return messageRepository.findAll().stream().filter(m -> m.getContent().contains(content))
        .map(MessageDto::from).toList();
  }

  @Override
  public List<MessageDto> findAllBySenderId(String senderId) {
    UserDto byId = userService.findById(senderId);
    if (byId == null) {
      //todo - 고민: 메세지를 검색할때 유저 아이디가 없다고 에러를 출력해야할까?
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    return messageRepository.findAll().stream().filter(m -> m.getAuthorId().equals(senderId))
        .map(MessageDto::from).toList();
  }

  @Override
  public List<MessageDto> findAllByCreatedAt(Instant createdAt) {
    return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals(createdAt))
        .map(MessageDto::from).toList();
  }

  @Override
  public List<MessageDto> findAllByChannelId(String channelId, String userId) {
    ChannelDto byId = channelService.findById(channelId, userId); //change Sign
    if (byId == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channelId))
        .map(MessageDto::from).toList();
  }

  @Override
  public MessageDto updateMessage(String messageId, UpdateMessageDto updateMessageDto)
      throws CustomException {
    Message message = messageRepository.findById(messageId);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    if (updateMessageDto.newContent().isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
    }
    if (!message.getAuthorId().equals(updateMessageDto.userId())) {
      throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    if (!message.getContent().equals(updateMessageDto.newContent())) {
      message.setContent(updateMessageDto.newContent());
      message.setUpdatedAt(updateMessageDto.updatedAt());
    }
    //todo
    //메세지의 이미지를 삭제하거나 추가하는 경우
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
    Message message = messageRepository.findById(messageId);
    if (message == null) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    if (!message.getAuthorId().equals(userId)) {
      throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
    }

    for (String imageId : message.getAttachmentImageIds()) {
      binaryContentService.deleteById(imageId);
    }

    return messageRepository.delete(messageId);
  }
}
