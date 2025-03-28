package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.MessageApiDocs;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageApiDocs {

  private final MessageService messageService;

  @Override
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CustomApiResponse<MessageResponse>> createMessage(
      @RequestPart(value = "messageCreateRequest") MessageRequest.Create messageRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files
  ) {
    log.info("POST /api/messages");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(messageService.createMessage(messageRequest, files)));
  }

  @Override
  @PutMapping(value = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CustomApiResponse<MessageResponse>> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageRequest.Update messageRequest
  ) {
    log.info("PUT /api/messages/{}", messageId);
    return ResponseEntity.ok(
        CustomApiResponse.success(messageService.update(messageId, messageRequest))
    );
  }

  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<CustomApiResponse<Void>> deleteMessage(@PathVariable UUID messageId) {

    messageService.deleteById(messageId);
    log.info("DELETE /api/messages/{}", messageId);
    return ResponseEntity.ok(CustomApiResponse.success("Message deleted successfully"));
  }

  @Override
  @GetMapping
  public ResponseEntity<CustomApiResponse<PageResponse<MessageResponse>>> getMessageListByChannel(
      @RequestParam("channelId") UUID channelId) {
    return ResponseEntity.ok(
        CustomApiResponse.success(messageService.findAllByChannelId(channelId))
    );
  }
}
