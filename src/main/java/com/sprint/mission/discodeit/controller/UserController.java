
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

// 사용자 관리 controller
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> create(
      @RequestPart("userRequest") CreateUserRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<CreateBinaryContentRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    UserDto userDto = userService.create(userRequest, profileRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userDto);
  }

  @Override
  @PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userRequest") UpdateUserRequest userRequest,
      @RequestPart("profile") MultipartFile profile) {
    Optional<CreateBinaryContentRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    UserDto userDto = userService.update(userId, userRequest, profileRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @Override
  @DeleteMapping(path = "{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userDtos = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDtos);
  }

  @Override
  @PatchMapping(path = "{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable("userId") UUID userId,
      @RequestBody UpdateUserStatusRequest request) {
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusDto);
  }


  private Optional<CreateBinaryContentRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        CreateBinaryContentRequest binaryContentRequest = new CreateBinaryContentRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}