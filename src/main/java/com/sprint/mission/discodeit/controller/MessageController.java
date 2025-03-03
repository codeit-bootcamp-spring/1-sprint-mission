package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @GetMapping("/{channelId}")
  public ResponseEntity<List<MessageSendRequest>> getMessages(
      @PathVariable("channelId") String channelId) {
    List<MessageSendRequest> messageDtos = messageService.findAllByChannelId(
        UUID.fromString(channelId));
    return ResponseEntity.ok(messageDtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MessageUpdateRequest> updateMessage(@PathVariable("id") String id,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    messageService.updateContent(messageUpdateRequest);
    return ResponseEntity.ok(messageUpdateRequest);
  }

  @PostMapping("/{channelId}")
  public ResponseEntity<MessageSendRequest> sendMessage(@PathVariable("channelId") String channelId,
      @RequestBody MessageSendRequest messageSendRequest) {
    if (messageSendRequest.replyToId() != null) {
      messageService.sendReplyMessage(messageSendRequest);
    } else {
      messageService.sendCommonMessage(messageSendRequest);
    }
    return ResponseEntity.ok(messageSendRequest);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeMessage(@PathVariable("id") UUID id) {
    messageService.remove(id);
    return ResponseEntity.ok().build();
  }

}
