package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new UserNotFoundException("Author with id " + authorId + " does not exist");
    }

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          BinaryContent binaryContent = getBinaryContent(attachmentRequest);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = Message.createMessage(
        content,
        channelId,
        authorId,
        attachmentIds
    );
    return messageRepository.save(message);
  }

  private static BinaryContent getBinaryContent(BinaryContentCreateRequest attachmentRequest) {
    String fileName = attachmentRequest.fileName();
    String contentType = attachmentRequest.contentType();
    byte[] bytes = attachmentRequest.bytes();
    return BinaryContent.createBinaryContent(
        fileName, (long) bytes.length, contentType, bytes);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .toList();
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.content();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException("Message with id " + messageId + " not found"));
    Message updatedMessage = message.update(newContent);
    return messageRepository.save(updatedMessage);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException("Message with id " + messageId + " not found"));

    message.getAttachmentIds()
        .forEach(binaryContentRepository::deleteById);

    messageRepository.deleteById(messageId);
  }
}
