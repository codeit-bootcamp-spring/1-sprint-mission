package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.CursorResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.CursorResponseMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelService channelService;
  private final UserService userService;

  @Override
  @Transactional
  public MessageResponse createMessage(CreateMessageRequest request) {
    Channel channel = channelService.getChannel(request.channelID());
    User author = userService.getUserById(request.authorID());
    Message newMessage = new Message(request.text(), author, channel);
    log.info("MESSAGE_CREATED - {} | {} | 내용: \"{}\"", author.getId(), newMessage.getId(),
        newMessage.getText());
    return MessageMapper.INSTANCE.toMessageResponse(messageRepository.save(newMessage));
  }

  @Override
  @Transactional(readOnly = true)
  public List<MessageResponse> getMessages() {
    return messageRepository.findAll().stream()
        .map(MessageMapper.INSTANCE::toMessageResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageResponse> getPageMessages(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Message> messages = messageRepository.findAll(pageable);
    return PageResponseMapper.fromPage(messages.map(MessageMapper.INSTANCE::toMessageResponse));
  }

  @Override
  @Transactional(readOnly = true)
  public CursorResponse<Message> getCursorPages(Instant cursor, int size) {
    Pageable pageable = PageRequest.of(0, size);
    Page<Message> messages = messageRepository.findAllByCursor(cursor, pageable);
    return CursorResponseMapper.fromPage(messages);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MessageResponse> getMessagesByChannel(UUID ChannelID) {
    return channelService.getMessagesFromChannel(ChannelID).stream()
        .map(MessageMapper.INSTANCE::toMessageResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public MessageResponse getMessage(UUID uuid) {
    return messageRepository.findById(uuid)
        .map(MessageMapper.INSTANCE::toMessageResponse).orElseThrow(
            () -> new EntityNotFoundException("Message not found")
        );
  }

  @Override
  @Transactional
  public MessageResponse updateMessage(UUID id, UpdateMessageRequest request) {
    return messageRepository.findById(id)
        .map(message -> {
          message.updateText(request.text());
          log.info("MESSAGE_EDITED - {} | {} | 변경 내용: \"{}\"", id, message.getId(),
              message.getText());
          return messageRepository.save(message);
        })
        .map(MessageMapper.INSTANCE::toMessageResponse).orElseThrow(
            () -> new EntityNotFoundException("Message not found")
        );
  }

  @Override
  @Transactional
  public void deleteMessage(UUID uuid) {
    messageRepository.deleteById(uuid);
  }
}
