package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @GetMapping("/data")
  public ResponseEntity<List<FindUserDto>> getUsersData() {
    List<FindUserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/findAll")
  public String getUsers() {
    return "user-list";
  }

  @GetMapping("/{id}")
  public ResponseEntity<FindUserDto> getUser(@PathVariable("id") UUID id) {
    FindUserDto user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<UserCreateRequest> createUser(
      @RequestBody UserCreateRequest createUserDto) {
    userService.createUser(createUserDto);
    return ResponseEntity.ok(createUserDto);
  }

  @PutMapping("/{id}/name")
  public ResponseEntity<UpdateUserDto> updateUser(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updateName(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @PutMapping("/{id}/password")
  public ResponseEntity<UpdateUserDto> updateUserPassword(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updatePassword(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<UpdateUserDto> updateUserStatus(@PathVariable("id") UUID id) {
    userStatusService.updateByUserId(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/profile-image")
  public ResponseEntity<UpdateUserDto> updateUserProfileImage(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updateProfileImage(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeUser(@PathVariable("id") UUID id) {
    userService.remove(id);
    return ResponseEntity.ok().build();
  }
}
