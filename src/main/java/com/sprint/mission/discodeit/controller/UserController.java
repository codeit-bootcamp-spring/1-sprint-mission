package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserSwagger;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.FileConverter;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserSwagger { //implements UserSwagger

  private final FileConverter fileConverter;
  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<UserCreateResponse> create(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile",
          required = false) MultipartFile profile
  ) {
    User user = userService.create(request, fileConverter.convertToBinaryRequest(profile));
    UserCreateResponse createResponse = UserCreateResponse.from(user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(createResponse);
  }
  /*
  UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
   */


  @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
  public ResponseEntity<UserUpdateResponse> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile",
          required = false) MultipartFile profile
  ) {

    User user = userService.update(userId, request,
        fileConverter.convertToBinaryRequest(profile));
    UserUpdateResponse updateResponse = UserUpdateResponse.from(user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(updateResponse);

  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusUpdateRequest> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.status(HttpStatus.OK)
        .body(UserStatusUpdateRequest.from(userStatus));

  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<UserDto> find(
      @PathVariable UUID userId
  ) {
    UserDto userDto = userService.find(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(userDto);
  }

//    @GetMapping
//  public ResponseEntity<UserListDto> findAllV0() {
//    UserListDto userListDto = userService.findAll();
//    return ResponseEntity.status(HttpStatus.OK)
//        .body(userListDto);
//  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> dtos = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK)
        .body(dtos);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
