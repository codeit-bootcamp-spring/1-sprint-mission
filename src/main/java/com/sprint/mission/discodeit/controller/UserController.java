package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserSwagger;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController implements UserSwagger {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(method = RequestMethod.POST, consumes =
      MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserCreateResponse> registerUser(
      @RequestPart(name = "user") UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {

    BinaryContentCreateRequest binaryContentCreateRequest = BinaryContentCreateRequest.EMPTY_REQUEST;
    if (profile != null) {
      binaryContentCreateRequest = getBinaryContentCreateRequest(profile);
    }

    User createdUser = userService.create(userCreateRequest, binaryContentCreateRequest);
    UserCreateResponse userCreateResponse = UserCreateResponse.from(createdUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(userCreateResponse);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
      MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserUpdateResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart(name = "user") UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {

    BinaryContentCreateRequest binaryContentCreateRequest = BinaryContentCreateRequest.EMPTY_REQUEST;
    if (profile != null) {
      binaryContentCreateRequest = getBinaryContentCreateRequest(profile);
    }

    User updatedUser = userService.update(userId, userUpdateRequest,
        binaryContentCreateRequest);
    UserUpdateResponse userUpdateResponse = UserUpdateResponse.from(updatedUser);

    return ResponseEntity.status(HttpStatus.OK).body(userUpdateResponse);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAllUser() {

    List<UserDto> allUsers = userService.findAll();

    return ResponseEntity.status(HttpStatus.OK).body(allUsers);
  }

  @RequestMapping(value = "{userId}/user-status",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.PATCH
  )
  public ResponseEntity<UserStatus> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {

    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    return ResponseEntity.status(HttpStatus.OK).body(userStatus);
  }

  private static BinaryContentCreateRequest getBinaryContentCreateRequest(
      MultipartFile multipartFile) {
    BinaryContentCreateRequest binaryContentCreateRequest;
    try {
      binaryContentCreateRequest = new BinaryContentCreateRequest(
          multipartFile.getOriginalFilename(),
          multipartFile.getContentType(),
          multipartFile.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentCreateRequest;
  }
}
