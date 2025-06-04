package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.UserApiDocs;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.facade.user.UserFacade;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

  private final UserFacade userFacade;


  @GetMapping("/{id}")
  @Override
  public ResponseEntity<UserDto> getUser(@PathVariable String id) {
    UserDto user = userFacade.findUserById(id);
    return ResponseEntity.ok(user);
  }

  @Override
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart("userCreateRequest")
      CreateUserRequest createUserRequest,
      @RequestPart(value = "profile", required = false)
      MultipartFile profile) {
    log.debug("[CREATE USER REQUEST] : [USERNAME: {}]] , [EMAIL: {}]", createUserRequest.username(),
        createUserRequest.email());

    UserDto user = userFacade.createUser(createUserRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);

  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    List<UserDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @Override
  @PatchMapping(
      value = "/{id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserDto> updateUser(
      @PathVariable String id,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      @Valid @RequestPart(value = "userUpdateRequest") UserUpdateDto updateDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    UserDto user = userFacade.updateUser(id, profile, updateDto, userDetails);
    return ResponseEntity.ok(user);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {

    log.debug("[DELETE USER REQUEST] : [ID: {}]", id);
    userFacade.deleteUser(id.toString());

    log.debug("[DELETED USER] : [ID: {}]", id);
    return ResponseEntity.noContent().build();
  }
}
