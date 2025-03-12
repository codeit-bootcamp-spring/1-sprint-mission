package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.MessageApiDocs;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageApiDocs {

  private final MessageService messageService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public ResponseEntity<CustomApiResponse<MessageResponse>> createMessage(
      @RequestPart(value = "messageCreateRequest") MessageRequest.Create messageRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(messageService.createMessage(messageRequest, files)));
  }

  @PutMapping(value = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public ResponseEntity<CustomApiResponse<MessageResponse>> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageRequest.Update messageRequest
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(messageService.update(messageId, messageRequest))
    );
  }

  @DeleteMapping("/{messageId}")
  @Override
  public ResponseEntity<CustomApiResponse<Void>> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteById(messageId);
    return ResponseEntity.ok(CustomApiResponse.success("Message deleted successfully"));
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<MessageResponse>>> getMessageListByChannel(
      @RequestParam("channelId") UUID channelId) {
    return ResponseEntity.ok(
        CustomApiResponse.success(messageService.findAllByChannelId(channelId))
    );
  }
}
