package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.MessageApiDocs;
import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
  public MessageResponse createMessage(
      @RequestPart MessageRequest.Create messageRequest,
      @RequestPart(value = "files", required = false) List<MultipartFile> files
  ) {
    return messageService.createMessage(messageRequest, files);
  }

  @PutMapping(value = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public MessageResponse updateMessage(
      @PathVariable UUID messageId,
      @RequestPart MessageRequest.Update messageRequest,
      @RequestPart(value = "files", required = false) List<MultipartFile> files
  ) {
    return messageService.update(messageId, messageRequest, files);
  }

  @DeleteMapping("/{messageId}")
  @Override
  public String deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteById(messageId);
    return "delete ok";
  }

  @GetMapping
  @Override
  public List<MessageResponse> getMessageListByChannel(@RequestParam("channelId") UUID channelId) {
    return messageService.findAllByChannelId(channelId);
  }
}
