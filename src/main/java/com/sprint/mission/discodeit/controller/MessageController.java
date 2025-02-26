package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.MessageApiDocs;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.service.facade.message.MessageMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController implements MessageApiDocs {

  private final MessageMasterFacade messageMasterFacade;

  @Override
  @PostMapping(
      value = "/messages",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MessageResponseDto> sendMessage(
      @RequestPart(value = "messageCreateRequest") CreateMessageDto messageDto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files){
    MessageResponseDto message = messageMasterFacade.createMessage(messageDto, files);
    return ResponseEntity.status(201).body(message);
  }


  @Override
  @PatchMapping("/messages/{messageId}")
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable String messageId, @RequestBody MessageUpdateDto messageDto){
    MessageResponseDto message = messageMasterFacade.updateMessage(messageId, messageDto);
    return ResponseEntity.ok(message);
  }

  @Override
  @DeleteMapping("/messages/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable String messageId){
    messageMasterFacade.deleteMessage(messageId);
    return ResponseEntity.status(204).build();
  }

  @Override
  @GetMapping("/messages")
  public ResponseEntity<List<MessageResponseDto>> getChannelMessages(@RequestParam String channelId){
    List<MessageResponseDto> messages = messageMasterFacade.findMessagesByChannel(channelId);
    return ResponseEntity.ok(messages);
  }
}
