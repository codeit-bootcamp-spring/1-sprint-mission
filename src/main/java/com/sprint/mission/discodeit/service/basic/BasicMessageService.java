package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;

import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


  private final MessageRepository messageRepository;
  private final MessageValidator messageValidator;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageDto create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    //messageValidator.validateMessage(dto.getContent(), dto.getAuthorId(), dto.getChannelId());
    User findUser = userRepository.findById(dto.getAuthorId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Channel findChannel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    Message message = new Message(dto.getContent(), findUser, findChannel);

    binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          BinaryContent binaryContent = new BinaryContent(
              attachmentRequest.getFileName(),
              attachmentRequest.getContentType(),
              (long) attachmentRequest.getBytes().length
          );

          BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(savedBinaryContent.getId(), attachmentRequest.getBytes());
          return savedBinaryContent;
        })
        .forEach(message::addAttachments);

    messageRepository.save(message);
    return messageMapper.toDto(message);
  }


  //no use
  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    return messageMapper.toDto(message);
  }


  //나중에 페이징해서 처리해야함
  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannel_Id(channelId).stream()
        .map(messageMapper::toDto).toList();
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateDTO dto) {
    Message findMessage = messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    findMessage.setMessage(dto.getNewContent());
    return messageMapper.toDto(findMessage);
  }

  @Override
  public void delete(UUID id) {
    //binaryContent ddl - on delete cascade
    messageRepository.deleteById(id);
  }


}
