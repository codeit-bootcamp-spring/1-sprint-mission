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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(method = RequestMethod.GET, value = "/json")
  public ResponseEntity<List<FindUserDto>> getUsersData() {
    List<FindUserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/findAll")
  public String getUsers() {
    return "user-list";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public ResponseEntity<FindUserDto> getUser(@PathVariable("id") UUID id) {
    FindUserDto user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<UserCreateRequest> createUser(
      @RequestBody UserCreateRequest createUserDto) {
    userService.createUser(createUserDto);
    return ResponseEntity.ok(createUserDto);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}/name")
  public ResponseEntity<UpdateUserDto> updateUser(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updateName(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}/password")
  public ResponseEntity<UpdateUserDto> updateUserPassword(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updatePassword(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}/status")
  public ResponseEntity<UpdateUserDto> updateUserStatus(@PathVariable("id") UUID id) {
    userStatusService.updateByUserId(id);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}/profile-image")
  public ResponseEntity<UpdateUserDto> updateUserProfileImage(@PathVariable("id") UUID id,
      @RequestBody UpdateUserDto updateUserDto) {
    userService.updateProfileImage(updateUserDto);
    return ResponseEntity.ok(updateUserDto);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public ResponseEntity<Void> removeUser(@PathVariable("id") UUID id) {
    userService.remove(id);
    return ResponseEntity.ok().build();
  }
}
