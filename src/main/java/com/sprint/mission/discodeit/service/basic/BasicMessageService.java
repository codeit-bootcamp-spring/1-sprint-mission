package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    Channel channel = messageCreateRequest.channel();
    User author = messageCreateRequest.author();
    if (!channelRepository.existsById(channel.getId())) {
      throw new NoSuchElementException("Channel with id " + channel.getId() + " does not exist");
    }
    if (!userRepository.existsById(author.getId())) {
      throw new NoSuchElementException("Author with id " + author.getId() + " does not exist");
    }

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          return binaryContentRepository.save(binaryContent);
        }).toList();
    String content = messageCreateRequest.content();
    Message message = new Message(content, channel, author, attachments);
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream().toList();
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.getAttachments().forEach(s->binaryContentRepository.deleteById(s.getId()));
    messageRepository.deleteById(messageId);
  }

  @Override
  public List<Message> findAll() {
    return new ArrayList<>(messageRepository.findAll());
  }
}
