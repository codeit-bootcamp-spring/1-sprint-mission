package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.UserApiDocs;
import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserStatusRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
  public List<UserResponse> getAllUser() {
    return userService.findAll();
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public UserResponse createUser(
      @RequestPart("user") UserRequest userRequest,
      @RequestPart(value = "image", required = false) MultipartFile userProfileImage
  ) {
    return userService.createUser(userRequest, userProfileImage);
  }

  @PutMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  @Override
  public UserResponse updateUser(
      @PathVariable UUID userId,
      @RequestPart("userRequest") UserRequest userRequest,
      @RequestPart(value = "image", required = false) MultipartFile userProfileImage
  ) {
    return userService.update(userId, userRequest, userProfileImage);
  }

  @DeleteMapping("/{userId}")
  @Override
  public String deleteUser(@PathVariable UUID userId) {
    userService.deleteById(userId);
    return "delete ok";
  }

  @PutMapping("/{userId}/userStatus")
  @Override
  public UserStatusResponse updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusRequest.Update request
  ) {
    return userStatusService.updateByUserId(userId, request);
  }

}
