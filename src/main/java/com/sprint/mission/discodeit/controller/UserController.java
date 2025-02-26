package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@ResponseBody
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<User> create(
      @ModelAttribute UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
    if (profile != null) {
      profileRequest = resolveProfileRequest(profile);
    }

    User createdUser = userService.create(userCreateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @PutMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  /*
  TODO @PathVariable은 URL 경로의 일부로 데이터를 전달하는 방식.
   multipart/form-data 요청에서는 URL이 아니라 요청 바디에서 데이터가 전달되기 때문에, 스프링이 @PathVariable을 제대로 매핑하지 못할 수도 있음.
   그래서 보통 파일 업로드가 포함된 경우 (이렇게 form data로 줘야하는 경우) @RequestParam을 사용해서 바디에서 값을 받아야 함.
   @RequestParam : multipart/form-data 요청에서는 URL이 아니라 "바디에서" 값을 받는다!
    -> GET 요청이면 URL에서 (?userId=...) 값을 받지만,
    -> POST (multipart/form-data) 요청이면 바디에서 값을 받는다!
   */
  public ResponseEntity<User> update(
      @RequestParam UUID userId,
      @ModelAttribute UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty(); // 초기화
    if (profile != null) {
      profileRequest = resolveProfileRequest(
          profile); // 받은 프로필 '파일'을 BinaryContentCreateRequest DTO로 변환
    }

    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> allUsers = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(allUsers);
  }

  @PutMapping("/{userId}/user-status")
  public ResponseEntity<Void> updateUserStatusByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
