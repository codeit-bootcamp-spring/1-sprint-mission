package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping(value = "", consumes = {
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<MessageDTO> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    Message message = messageService.create(messageCreateRequest, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(MessageDTO.fromEntity(message));
  }

  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDTO> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message message = messageService.update(messageId, request);
    return ResponseEntity.status(HttpStatus.OK).body(MessageDTO.fromEntity(message));
  }

  @DeleteMapping(value = "/{messageId}/{writerId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID messageId,
      @PathVariable("writerId") UUID writerId) {
    messageService.delete(messageId, writerId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(value = "/{channelId}")
  public ResponseEntity<List<MessageDTO>> getChannelMessages(
      @PathVariable("channelId") UUID channelId) {
    return ResponseEntity.status(HttpStatus.OK).body(messageService.findByChannel(channelId));
  }

}
