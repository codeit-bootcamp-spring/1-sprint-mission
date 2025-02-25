package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageSwagger;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageSwagger {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes =
      MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> sendMessage(
      @RequestPart("message") MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> files
  ) {
    List<BinaryContentCreateRequest> filesRequest = Collections.emptyList();
    if (files != null) {
      filesRequest = files.stream()
          .map(MessageController::getBinaryContentCreateRequest)
          .toList();
    }

    Message sendMessage = messageService.create(messageCreateRequest, filesRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(sendMessage);
  }

  @RequestMapping(value = "/{messageId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.PATCH)
  public ResponseEntity<Message> editMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);

    return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> readAllMessage(@PathVariable UUID channelId) {
    List<Message> allMessage = messageService.findAllByChannelId(channelId);

    return ResponseEntity.status(HttpStatus.OK).body(allMessage);
  }

  private static BinaryContentCreateRequest getBinaryContentCreateRequest(
      MultipartFile multipartFile) {
    BinaryContentCreateRequest binaryContentCreateRequest;
    try {
      binaryContentCreateRequest = new BinaryContentCreateRequest(
          multipartFile.getOriginalFilename(),
          multipartFile.getContentType(),
          multipartFile.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentCreateRequest;
  }
}
