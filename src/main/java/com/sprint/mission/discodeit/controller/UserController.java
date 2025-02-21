package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.openapi.UserApiDocs;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

  private final UserMasterFacade userFacade;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
    UserResponseDto user = userFacade.findUserById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<CreateUserResponse> createUser(
      @RequestPart("userCreateRequest") CreateUserRequest createUserRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws JsonProcessingException {

    CreateUserResponse user = userFacade.createUser(createUserRequest, profile);
    return ResponseEntity.status(201).body(user);
  }



  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    List<UserResponseDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CreateUserResponse> updateUser(
      @PathVariable String id,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      @RequestPart(value = "userUpdateRequest") UserUpdateDto updateDto

  ) {

    CreateUserResponse user = userFacade.updateUser(id, profile, updateDto);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    userFacade.deleteUser(id);
    return ResponseEntity.ok("Successfully Deleted");
  }

}
