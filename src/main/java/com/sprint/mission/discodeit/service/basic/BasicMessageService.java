package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  @Override
  public Message createMessage(MessageCreateDTO messageCreateDTO) {
    Channel foundChannel = channelRepository.findById(messageCreateDTO.channelId()).orElseThrow(()
        -> new NoSuchElementException(messageCreateDTO.channelId() + "does not exist"));

    User foundUser = userRepository.findById(messageCreateDTO.userId()).orElseThrow(()
        -> new NoSuchElementException(messageCreateDTO.userId() + "does not exist"));

    //builder를 통한 message만들기
    Message message = Message.builder()
        .content(messageCreateDTO.content())
        .channel(foundChannel)
        .author(foundUser)
        .attachments(messageCreateDTO.attachments()).build();

    return messageRepository.save(message);
  }

  @Override
  public Message findById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findByChannelId(channelId);
  }

  @Override
  public List<Message> findAll() {
    return messageRepository.findAll();
  }

  @Override
  public Message update(MessageUpdateDTO messageUpdateDTO) {
    Message message = messageRepository.findById(messageUpdateDTO.uuid())
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));

    message.updateContent(messageUpdateDTO.content(), messageUpdateDTO.binaryContentList());
    return messageRepository.save(message);
  }

  @Override
  public void deleteMessage(UUID msgID) {
    messageRepository.deleteById(msgID);
  }
}
