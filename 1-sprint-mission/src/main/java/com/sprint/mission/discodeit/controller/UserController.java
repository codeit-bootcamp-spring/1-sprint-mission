package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
@Tag(name = "UserController", description = "UserController API 목록")
public class UserController {

  private final BasicUserService userService;

  @GetMapping
  public ResponseEntity<Void> showInfoAllUser() {
    try {
      userService.showAllUser();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @GetMapping("/{userName}")
  public ResponseEntity<Void> showInfo(@PathVariable String userName) {
    UserDto userDto = new UserDto();
    userDto.setUserName(userName);
    try {
      userService.showInfoUser(userDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
    try {
      userService.creatUser(userDto);

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody UserDto userDto) {
    try {
      userService.modifyUser(userDto);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }


  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    UserDto userDto = new UserDto();
    userDto.setId(userId);

    try {
      userService.deleteUser(userDto);
    } catch (IOException | ClassNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();

  }

}
