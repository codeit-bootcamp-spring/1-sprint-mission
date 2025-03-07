package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelService channelService;
  private final UserService userService;

  @Override
  public MessageResponse createMessage(CreateMessageRequest request) {
    Channel channel = channelService.getChannel(request.channelID());
    User author = userService.getUserById(request.authorID());
    Message newMessage = new Message(request.text(), author, channel);
    return MessageResponse.fromEntity(messageRepository.save(newMessage));
  }

  @Override
  public List<MessageResponse> getMessages() {
    return messageRepository.findAll().stream()
        .map(MessageResponse::fromEntity)
        .toList();
  }

  @Override
  public List<MessageResponse> getMessagesByChannel(UUID ChannelID) {
    return channelService.getMessagesFromChannel(ChannelID).stream()
        .map(MessageResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public MessageResponse getMessage(UUID uuid) {
    return messageRepository.findById(uuid)
        .map(MessageResponse::fromEntity).orElseThrow(
            () -> new EntityNotFoundException("Message not found")
        );
  }

  @Override
  public MessageResponse updateMessage(UUID id, UpdateMessageRequest request) {
    return messageRepository.findById(id)
        .map(message -> {
          message.updateText(request.text());
          return messageRepository.save(message);
        })
        .map(MessageResponse::fromEntity).orElseThrow(
            () -> new EntityNotFoundException("Message not found")
        );
  }

  @Override
  public void deleteMessage(UUID uuid) {
    messageRepository.deleteById(uuid);
  }
}
