package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  //  메시지 생성
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageService.createMessage(messageCreateRequest, attachmentRequests));
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message updatedMessage = messageService.updateMessage(messageId, request);
    return updatedMessage != null ? ResponseEntity.ok(updatedMessage)
        : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/channel/{channelId}")
  public ResponseEntity<List<Message>> getMessageByChannelId(
      @PathVariable UUID channelId) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }
}
