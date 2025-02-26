package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.message.MessageFindBResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Message> createMessage(
      @RequestPart(value = "messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "binaryContents", required = false) List<MultipartFile> files)
      throws Exception {

    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        binaryContentCreateRequests.add(new BinaryContentCreateRequest(file));
      }
    }

    return ResponseEntity.status(HttpStatus.CREATED).body( // 201
        messageService.createMessage(messageCreateRequest, binaryContentCreateRequests));
  }

  @PutMapping(value = "/{messageId}")
  public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    return ResponseEntity.ok(messageService.updateMessageText(messageId,
        messageUpdateRequest)); // 스프린트 미션 5 심화 조건 중 API 스펙을 준수
  }

  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessageById(messageId);
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping
  public ResponseEntity<List<Message>> getMessageByChannelId(
      @RequestParam UUID channelId) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId)); // 200
  }
}
