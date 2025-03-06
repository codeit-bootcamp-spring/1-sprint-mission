package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.UserApiDocs;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserStatusRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<UserResponse>>> getAllUser() {
    return ResponseEntity.ok(CustomApiResponse.success(userService.findAll()));
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public ResponseEntity<CustomApiResponse<UserResponse>> createUser(
      @RequestPart("user") UserRequest userRequest,
      @RequestPart(value = "image", required = false) MultipartFile userProfileImage
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(userService.createUser(userRequest, userProfileImage)));
  }

  @PutMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public ResponseEntity<CustomApiResponse<UserResponse>> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userRequest") UserRequest userRequest,
      @RequestPart(value = "image", required = false) MultipartFile userProfileImage
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(userService.update(userId, userRequest, userProfileImage))
    );
  }

  @DeleteMapping("/{userId}")
  @Override
  public ResponseEntity<CustomApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
    userService.deleteById(userId);
    return ResponseEntity.ok(CustomApiResponse.success("User deleted successfully"));
  }

  @PutMapping("/{userId}/userStatus")
  @Override
  public ResponseEntity<CustomApiResponse<UserStatusResponse>> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusRequest.Update request
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(userStatusService.updateByUserId(userId, request))
    );
  }

}
