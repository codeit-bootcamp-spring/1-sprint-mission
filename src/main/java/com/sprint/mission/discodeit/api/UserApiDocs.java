package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
@RequestMapping("/api/users")
public interface UserApiDocs {

  @Operation(summary = "전체 User 목록 조회", description = "시스템 내 모든 User 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
  })
  @GetMapping
  ResponseEntity<List<UserDto>> findAll(Model model);

  @Operation(summary = "User 등록", description = "새로운 User를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
  })
  @PostMapping
  ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @Operation(summary = "User 삭제", description = "특정 ID를 가진 User를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  })
  @DeleteMapping("/{userId}")
  ResponseEntity<Void> delete(@PathVariable UUID userId);

  @Operation(summary = "User 정보 수정", description = "User 정보를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 정보 수정 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  })
  @PatchMapping("/{userId}")
  ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @Operation(summary = "User 온라인 상태 업데이트", description = "User의 온라인 상태를 업데이트합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 상태 업데이트 성공"),
      @ApiResponse(responseCode = "404", description = "User 상태를 찾을 수 없음")
  })
  @PatchMapping("/{userId}/userStatus")
  ResponseEntity<UserStatus> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  );
}
