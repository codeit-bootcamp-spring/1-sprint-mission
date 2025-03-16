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
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    // Channel과 User 객체 조회
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));

    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> new NoSuchElementException("Author with id " + authorId + " does not exist"));

    // BinaryContent 객체 리스트 생성
    List<BinaryContent> attachments = new ArrayList<>();
    if (binaryContentCreateRequests != null && !binaryContentCreateRequests.isEmpty()) {
      for (BinaryContentCreateRequest request : binaryContentCreateRequests) {
        BinaryContent binaryContent = new BinaryContent(
            request.fileName(),
            (long) request.bytes().length,
            request.contentType(),
            request.bytes()
        );
        attachments.add(binaryContentRepository.save(binaryContent));
      }
    }

    // Channel, User, BinaryContent 객체 리스트 직접 전달
    Message message = new Message(
        messageCreateRequest.content(),
        channel,
        author,
        attachments
    );
    return messageRepository.save(message);
  }

  @Override
  @Transactional
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  @Transactional
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .toList();
  }

  @Override
  @Transactional
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    // getAttachmentIds() 대신 getAttachments()로 접근하여 ID 가져오기
    if (message.getAttachments() != null) {
      message.getAttachments().forEach(attachment ->
          binaryContentRepository.deleteById(attachment.getId())
      );
    }

    messageRepository.deleteById(messageId);
  }
}