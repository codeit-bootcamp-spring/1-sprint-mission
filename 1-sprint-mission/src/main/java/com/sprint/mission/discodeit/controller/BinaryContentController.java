package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContent")
@Tag(name = "BinaryContentController", description = "BinaryContentController API 목록")
public class BinaryContentController {

  private final BasicBinaryContentService service;

  @GetMapping("/{messageId}")
  public ResponseEntity<Void> findContentByMessage(@PathVariable UUID messageId) {
    MessageDto messageDto = new MessageDto();
    messageDto.setMessageId(messageId);
    try {
      System.out.println(service.findByMessage(messageDto));
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @GetMapping("/{userId}")
  public ResponseEntity<Void> findContentByUser(@PathVariable UUID userId) {
    UserDto userDto = new UserDto();
    userDto.setId(userId);
    try {
      System.out.println(service.findByUser(userDto));

    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }
}
