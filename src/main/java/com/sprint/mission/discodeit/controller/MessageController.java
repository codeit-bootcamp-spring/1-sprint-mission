package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageSwagger;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageListResponse;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.FileConverter;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageSwagger {

  // implements MessageSwagger
  private final FileConverter fileConverter;
  private final BasicMessageService messageService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<MessageResponse> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> binaryContentRequest = new ArrayList<>();
    if (attachments != null) {
      binaryContentRequest = attachments.stream()
          .map(fileConverter::convertToBinaryRequest)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .toList();
    }
    Message message = messageService.create(request, binaryContentRequest);

    MessageResponse response = MessageResponse.from(message);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageResponse> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message message = messageService.update(messageId, request);
    MessageResponse response = MessageResponse.from(message);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping(value = "/{messageId}")
  public ResponseEntity<MessageResponse> find(
      @PathVariable UUID messageId
  ) {
    Message message = messageService.find(messageId);
    return ResponseEntity.ok(MessageResponse.from(message));
  }

  //    @GetMapping(value = "/channels/{channelId}")
  public ResponseEntity<MessageListResponse> findMessagesByChannelV0(
      @PathVariable UUID channelId
  ) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(MessageListResponse.from(messages));
  }

  @GetMapping
  public ResponseEntity<List<MessageResponse>> findMessagesByChannel(
      @RequestParam UUID channelId
  ) {
    List<MessageResponse> messages = messageService.findAllByChannelId(channelId)
        .stream().map(MessageResponse::from).toList();
    return ResponseEntity.ok(messages);
  }

  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

}