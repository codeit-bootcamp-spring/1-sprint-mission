package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/status")
@Tag(name = "UserStatusController", description = "UserStatusController API 목록")
public class UserStatusController {

  private final BasicUserStatusService basicUserStatusService;

  @PutMapping
  public ResponseEntity<Void> login(@RequestBody UserDto userDto) {
    try {
      basicUserStatusService.updateByUserId(userDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
