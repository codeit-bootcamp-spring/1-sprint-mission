package com.sprint.mission.discodeit.controller.legacy;

import com.sprint.mission.discodeit.entity.message.*;
import com.sprint.mission.discodeit.entity.message.create.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.message.create.MessageCreateResponse;
import com.sprint.mission.discodeit.factory.MessageRequestMapper;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Hidden
@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final MessageRequestMapper messageRequestMapper;

  @RequestMapping(method = RequestMethod.POST)
  public MessageCreateResponse sendMessage(@RequestBody MessageCreateRequest request)
      throws IOException {
    return messageService.create(request, null);
  }

  @RequestMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public MessageCreateResponse sendMessageWithFile(
      @RequestPart(value = "request") String requestStr,
      @RequestPart(value = "file") List<MultipartFile> files) throws IOException {

    // string -> MessageCreateRequest
    MessageCreateRequest request = messageRequestMapper.stringToJson(requestStr,
        MessageCreateRequest.class);
    MessageSendFileRequest fileRequest = new MessageSendFileRequest(
        files.get(0).getOriginalFilename(), null, files);

    return messageService.create(request, fileRequest);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
  public MessageResponse updateMessage(
      @PathVariable("messageId") String messageId,
      @Validated @RequestBody MessageUpdateRequest request) {

    return messageService.updateMessage(messageId, request);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public UUID deleteMessage(@PathVariable("messageId") String messageId) {
    return messageService.deleteMessage(messageId);
  }

  @GetMapping("/ch/{channelId}")
  public Map<UUID, MessageResponse> getAllMessages(@PathVariable("channelId") String channelId) {
    return messageService.findAllMessageByChannelId(channelId);
  }

  @GetMapping("/{messageId}")
  public MessageResponse getMessage(@PathVariable("messageId") String messageId) {
    return messageService.getMessageById(UUID.fromString(messageId));
  }
}