package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
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

@Tag(name = "사용자 관리", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "사용자 생성 성공")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> createUser(
      @RequestPart("user") @Parameter(schema = @Schema(type = "string", format = ("binary"))) CreateUserRequest request,
      @RequestPart(value = "file", required = false) @Parameter(description = "업로드할 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile file)
      throws IOException {
    CreateBinaryContentRequest binaryRequest =
        file != null ? new CreateBinaryContentRequest(file.getOriginalFilename(),
            file.getContentType(), file.getBytes()) : null;
    UserResponse userResponse = userService.createUser(request, Optional.ofNullable(binaryRequest));
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
  }

  @Operation(summary = "특정 사용자 조회", description = "사용자 ID를 이용하여 특정 사용자를 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(
      @Parameter(description = "조회할 사용자 ID", required = true) @PathVariable UUID id) {
    return userService.findUserById(id).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "전체 사용자 조회", description = "등록된 모든 사용자를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @Operation(summary = "사용자 삭제", description = "사용자 ID를 이용하여 특정 사용자를 삭제합니다.")
  @ApiResponses({@ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "삭제할 사용자 ID", required = true) @PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  @PatchMapping(value = "{id}", consumes = "multipart/form-data")
  public ResponseEntity<UserResponse> updateUser(
      @Parameter(description = "수정할 사용자 ID", required = true) @PathVariable UUID id,
      @RequestPart("user") UpdateUserRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
    CreateBinaryContentRequest binaryRequest =
        file != null ? new CreateBinaryContentRequest(file.getOriginalFilename(),
            file.getContentType(), file.getBytes()) : null;
    return userService.updateUser(id, request, Optional.ofNullable(binaryRequest))
        .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "사용자 접속 상태 수정", description = "사용자의 접속 상태 정보를 수정합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 접속 정보 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserUserStatus(
      @Parameter(description = "접속 상태 수정할 사용자 ID", required = true) @PathVariable UUID userId,
      @Parameter(description = "접속 상태 수정 dto", required = true) @RequestBody UpdateUserStatusRequest request) {
    return ResponseEntity.ok(userStatusService.update(userId, request));
  }
}
