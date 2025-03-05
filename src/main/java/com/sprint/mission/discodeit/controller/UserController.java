package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "User 등록", description = "새로운 사용자를 등록합니다.")
  @ApiResponse(responseCode = "201", description = "User 생성 성공")
  @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") String jsonRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws JsonProcessingException {
    UserCreateRequest request = objectMapper.readValue(jsonRequest, UserCreateRequest.class);

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User createdUser = userService.create(request, profileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Operation(summary = "User 정보 수정", description = "사용자의 정보를 수정합니다.")
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> update(@PathVariable("id") UUID id,
      @RequestPart("userUpdateRequest") String jsonRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws JsonProcessingException {
    UserUpdateRequest request = objectMapper.readValue(jsonRequest, UserUpdateRequest.class);

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User updatedUser = userService.update(id, request, profileRequest);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
  }


  @Operation(summary = "User 삭제", description = "사용자를 삭제합니다.")
  @ApiResponse(responseCode = "204", description = "User 삭제 성공")
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "전체 User 목록 조회", description = "모든 사용자 정보를 가져옵니다.")
  @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UserDto.class)))
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Operation(summary = "User 온라인 상태 업데이트", description = "특정 사용자의 온라인 상태를 업데이트합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 온라인 상태 업데이트 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/status/{id}")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID", required = true) @PathVariable("id") UUID id,
      @RequestPart String jsonRequest) throws JsonProcessingException {
    UserStatusUpdateRequest request = objectMapper.readValue(jsonRequest,
        UserStatusUpdateRequest.class);
    UserStatus updatedUserStatus = userStatusService.updateByUserId(id, request);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
  }


  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(), profileFile.getContentType(),
            profileFile.getBytes());
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
