package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.UserApiDocs;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.facade.user.UserMasterFacade;
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
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

  private final UserMasterFacade userFacade;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
    UserResponseDto user = userFacade.findUserById(id);
    return ResponseEntity.ok(user);
  }

  @Override
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<CreateUserResponse> createUser(

      @RequestPart("userCreateRequest")
      CreateUserRequest createUserRequest,

      @RequestPart(value = "profile", required = false)
      MultipartFile profile)
  {
    CreateUserResponse user = userFacade.createUser(createUserRequest, profile);
    return ResponseEntity.status(201).body(user);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    List<UserResponseDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @Override
  @PatchMapping(
      value = "/{id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<CreateUserResponse> updateUser(
      @PathVariable String id,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      @RequestPart(value = "userUpdateRequest") UserUpdateDto updateDto

  ) {

    CreateUserResponse user = userFacade.updateUser(id, profile, updateDto);
    return ResponseEntity.ok(user);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userFacade.deleteUser(id);
    return ResponseEntity.status(204).build();
  }

}
