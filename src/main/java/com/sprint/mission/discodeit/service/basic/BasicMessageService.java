package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.message.MessageFindBResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.embedded.TomcatVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.stereotype.Service;
//
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final InputHandler inputHandler;

  @Override
  public Message createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    if (channelRepository.findChannelById(messageCreateRequest.channelId()).isEmpty()) {
      throw new IllegalArgumentException("Channel이 존재하지 않습니다.");
    }
    if (userRepository.findUserById(messageCreateRequest.authorId()).isEmpty()) {
      throw new IllegalArgumentException("User가 존재하지 않습니다.");
    }

    Message message = new Message(messageCreateRequest.channelId(),
        messageCreateRequest.authorId(),
        messageCreateRequest.content(),
        binaryContentCreateRequests.stream()
            .map(binaryContentService::createBinaryContent)
            .map(BinaryContent::getId)
            .toList()
    );

    messageRepository.saveMessage(message);

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    return new MessageCreateResponse(
        messageCreateRequest.channelId(),
        messageCreateRequest.authorId(),
        messageCreateRequest.content(),
        message.getAttachmentIds()
    );
     */

    return message;
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    return messageRepository.findMessagesByChannelId(channelId).stream()
        .map(message ->
            new MessageFindBResponse(
                message.getId(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getMessageText()))
        .toList();
     */
    return messageRepository.findMessagesByChannelId(channelId);
  }

  @Override
  public MessageFindBResponse getMessageById(UUID id) {
    Message message =
        messageRepository.findMessageById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 메세지가 없습니다."));
    return new MessageFindBResponse(
        message.getId(),
        message.getChannelId(),
        message.getAuthorId(),
        message.getContent());
  }

  @Override
  public Message updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    // String messageText = inputHandler.getNewInput();
    Optional<Message> messageOptional = messageRepository.findMessageById(messageId);

    Message message = messageOptional.orElseThrow(
        () -> new NoSuchElementException("메세지가 존재하지 않습니다."));

    message.updateMessageText(messageUpdateRequest.newMessage());

    return messageRepository.saveMessage(message);
  }

  @Override
  public void deleteMessageById(UUID id) {
    String keyword = inputHandler.getYesNOInput();
    if (keyword.equalsIgnoreCase("y")) {

      if (messageRepository.findMessageById(id).isEmpty()) {
        throw new NoSuchElementException("메세지(" + id + ")를 찾을 수 없습니다.");
      }

      messageRepository.findMessageById(id).stream()
          .map(Message::getAttachmentIds)
          .flatMap(List::stream) // List에서 하나씩
          .forEach(binaryContentService::deleteBinaryContentById);

      messageRepository.deleteMessageById(id);
    }
  }
}
