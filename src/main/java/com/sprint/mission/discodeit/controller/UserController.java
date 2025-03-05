package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(value = "", method = RequestMethod.POST,
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> createUser(
      @RequestPart("userCreateRequest") CreateUserRequest createUserRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.createUser(createUserRequest, profileImage));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH,
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> updateUser(
      @PathVariable UUID id,
      @RequestPart(value = "userUpdateRequest") UpdateUserRequest updateUserRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.updateUser(id, updateUserRequest, profileImage));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<FindUserResponse>> findAllUsers() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.findAllUsers());
  }

  @RequestMapping(value = "/{id}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable UUID id,
      @RequestBody UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userStatusService.updateUserStatusByUserId(id, updateUserStatusByUserIdRequest));
  }
}
