package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.UserApiDocs;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.facade.user.UserMasterFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController implements UserApiDocs {

  private final UserMasterFacade userFacade;

  @GetMapping("/users/{id}")
  @Override
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
    UserResponseDto user = userFacade.findUserById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/users")
  public ResponseEntity<UserResponseDto> createUser(@Valid @ModelAttribute CreateUserRequest userDto) {
    UserResponseDto user = userFacade.createUser(userDto);
    return ResponseEntity.ok(user);
  }


  @GetMapping("/users")
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    List<UserResponseDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @Valid @ModelAttribute UserUpdateDto userDto) {
    UserResponseDto user = userFacade.updateUser(id, userDto);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id, @RequestParam String password) {
    try {
      userFacade.deleteUser(id, password);
      return ResponseEntity.ok("Successfully Deleted");
    } catch (UserValidationException e) {
      return ResponseEntity.badRequest().body("Failed To Delete");
    }
  }

}
