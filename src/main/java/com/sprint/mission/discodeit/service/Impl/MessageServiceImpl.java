package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public MessageDto createMessage(MessageDto dto) {
    Channel channel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "Channel not found"));

    User sender = userRepository.findById(dto.getAuthorId())
        .orElseThrow(() -> new RestApiException(DomainErrorCode.USER_NOT_FOUND, "User not found"));

    Message message = Message.builder()
        .channel(channel)
        .author(sender)
        .content(dto.getContent())
        .build();

    Message saved = messageRepository.save(message);

    return messageMapper.toDto(saved);
  }

  @Override
  public List<MessageDto> getChannelMessages(UUID channelId) {
    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    return messages.stream()
        .map(messageMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public MessageDto updateMessage(UUID id, MessageDto messageDTO) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "Message not found"));

    message.setContent(messageDTO.getContent());

    Message updatedMessage = messageRepository.save(message);
    return messageMapper.toDto(updatedMessage);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId)
        .stream()
        .map(messageMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteMessage(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "Message not found"));
    
    messageRepository.deleteById(id);
  }

  @Override
  public List<MessageDto> findAll() {
    return messageRepository.findAll()
        .stream()
        .map(messageMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public MessageDto getMessageById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "Message not found"));
    
    return messageMapper.toDto(message);
  }
}