package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Tag(name = "MessageController", description = "MessageController API 목록")
public class MessageController {

  private final BasicMessageService messageService;

  @GetMapping("/{messageId}")
  public ResponseEntity<Void> showInfo(@PathVariable UUID messageId) {
    MessageDto messageDto = new MessageDto();
    messageDto.setMessageId(messageId);

    try {
      messageService.showInfoMessage(messageDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @GetMapping("/channel/{channelName}")
  public ResponseEntity<Void> showInfoOfChannelMessage(@PathVariable String channelName) {
    MessageDto messageDto = new MessageDto();
    messageDto.setChannelName(channelName);

    try {
      messageService.showInfoChannelMessage(messageDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody MessageDto messageDto) {
    try {
      messageService.saveMessage(messageDto);

    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody MessageDto messageDto) {
    try {
      messageService.modifyMessage(messageDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    MessageDto messageDto = new MessageDto();
    messageDto.setMessageId(messageId);

    try {
      messageService.deleteMessage(messageDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

}
