package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.MessageService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @RequestMapping(value = "", method = RequestMethod.POST, consumes = {
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart(value = "messageCreateRequest") CreateMessageRequest createMessageRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageService.createMessage(createMessageRequest, multipartFileList));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID id,
      @RequestBody UpdateMessageRequest updateMessageRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(messageService.updateMessage(id, updateMessageRequest));
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
    messageService.deleteMessage(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<MessageDto>> findMessagesByChannel(
      @RequestParam("channelId") UUID channelId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(messageService.findAllMessagesByChannelId(channelId));
  }
}
