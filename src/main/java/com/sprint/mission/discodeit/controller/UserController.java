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
      @RequestPart("userCreateRequest") String userDtoJson,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();
    CreateUserRequest userDto = objectMapper.readValue(userDtoJson, CreateUserRequest.class);

    CreateUserResponse user = userFacade.createUser(userDto, profile);
    return ResponseEntity.ok(user);
  }



  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    List<UserResponseDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @Valid @ModelAttribute UserUpdateDto userDto) {
    UserResponseDto user = userFacade.updateUser(id, userDto);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id, @RequestParam String password) {
    try {
      userFacade.deleteUser(id, password);
      return ResponseEntity.ok("Successfully Deleted");
    } catch (UserValidationException e) {
      return ResponseEntity.badRequest().body("Failed To Delete");
    }
  }

}
