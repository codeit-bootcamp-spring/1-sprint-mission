package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "유저 관련 정보")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "사용자 생성",
      description = "선택적 프로필 이미지와 함께 새로운 사용자를 생성합니다.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "사용자 생성 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "잘못된 사용자 데이터",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "User with email {email} already exists"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResponse> create(
      @Parameter(description = "사용자 생성 정보")
      @RequestPart("userCreateRequest") String userCreateRequestStr,
      @Parameter(description = "선택적 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      UserCreateRequest userCreateRequest = objectMapper.readValue(userCreateRequestStr,
          UserCreateRequest.class);

      BinaryContentCreateRequest imageRequest = null;
      if (profile != null && !profile.isEmpty()) {
        imageRequest = new BinaryContentCreateRequest(
            profile.getOriginalFilename(),
            profile.getContentType(),
            profile.getBytes()
        );
      }

      UserResponse createdUser = userService.createUserWithProfileImage(userCreateRequest,
          imageRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    } catch (IOException e) {
      throw new IllegalArgumentException("Error processing profile image: " + e.getMessage());
    }
  }

  @GetMapping
  @Operation(
      summary = "전체 사용자 조회",
      description = "모든 사용자를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class)
              )
          )
      }
  )
  public ResponseEntity<List<UserResponse>> findAll() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }


  @PutMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "사용자 정보 수정",
      description = "선택적 프로필 이미지와 함께 사용자 정보를 수정합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 정보 수정 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "사용자를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "User with id {userId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResponse> update(
      @Parameter(description = "수정할 사용자의 ID")
      @PathVariable("userId") UUID userId,
      @Parameter(description = "사용자 정보 수정 내용")
      @RequestPart("userUpdateRequest") String userUpdateRequestStr,
      @Parameter(description = "선택적 새 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      UserUpdateRequest userUpdateRequest = objectMapper.readValue(userUpdateRequestStr,
          UserUpdateRequest.class);

      BinaryContentCreateRequest imageRequest = null;
      if (profile != null && !profile.isEmpty()) {
        imageRequest = new BinaryContentCreateRequest(
            profile.getOriginalFilename(),
            profile.getContentType(),
            profile.getBytes()
        );
      }

      UserResponse updatedUser = userService.updateUserWithProfileImage(userId, userUpdateRequest,
          imageRequest);
      if (updatedUser == null) {
        throw new NoSuchElementException("User with id " + userId + " not found");
      }
      return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @DeleteMapping("/{userId}")
  @Operation(
      summary = "사용자 삭제",
      description = "특정 사용자를 삭제합니다.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "사용자 삭제 성공"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "사용자를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "User with id {userId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 사용자의 ID")
      @PathVariable("userId") UUID userId) {
    boolean deleted = userService.deleteUser(userId);
    if (deleted) {
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } else {
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
  }

  @PatchMapping("/{userId}/userStatus")
  @Operation(
      summary = "사용자 상태 수정",
      description = "특정 사용자의 상태를 수정합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 상태 수정 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserStatus.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "사용자를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "User with id {userId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(description = "상태를 수정할 사용자의 ID")
      @PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    if (updatedUserStatus == null) {
      throw new NoSuchElementException("UserStatus with userId " + userId + " not found");
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }
}