package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.CursorResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @Override
  public ResponseEntity<MessageResponse> createMessage(CreateMessageRequest request) {
    MessageResponse response = messageService.createMessage(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<MessageResponse>> getAllMessages() {
    return ResponseEntity.ok(messageService.getMessages());
  }

  @Override
  public ResponseEntity<PageResponse<MessageResponse>> getAllPagingMessages(int page, int size) {
    return ResponseEntity.ok(messageService.getPageMessages(page, size));
  }

  @Override
  public ResponseEntity<CursorResponse<Message>> getAllCursorMessages(Instant cursor, int size) {
    return ResponseEntity.ok(messageService.getCursorPages(cursor, size));
  }

  @Override
  public ResponseEntity<MessageResponse> getMessageById(UUID id) {
    return ResponseEntity.ok(messageService.getMessage(id));
  }

  @Override
  public ResponseEntity<List<MessageResponse>> getMessagesByChannel(UUID channelId) {
    return ResponseEntity.ok(messageService.getMessagesByChannel(channelId));
  }

  @Override
  public ResponseEntity<MessageResponse> updateMessage(UUID id, UpdateMessageRequest request) {
    return ResponseEntity.ok(messageService.updateMessage(id, request));
  }

  @Override
  public ResponseEntity<Void> deleteMessage(UUID id) {
    messageService.deleteMessage(id);
    return ResponseEntity.noContent().build();
  }
}
