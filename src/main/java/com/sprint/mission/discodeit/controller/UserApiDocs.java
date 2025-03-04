package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "사용자 관리 API (생성, 수정, 삭제, 조회, 상태 업데이트)")
public interface UserApiDocs {

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
  ResponseEntity<User> create(
      @Parameter(description = "사용자 생성 요청 정보", required = true)
      UserCreateRequest userCreateRequest,

      @Parameter(description = "사용자 프로필 이미지 (선택 사항)")
      MultipartFile profile
  );

  @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보를 수정합니다.")
  ResponseEntity<User> update(
      @Parameter(description = "수정할 사용자 ID", required = true)
      UUID userId,

      @Parameter(description = "사용자 수정 요청 정보", required = true)
      UserUpdateRequest userUpdateRequest,

      @Parameter(description = "새로운 프로필 이미지 (선택 사항)")
      MultipartFile profile
  );

  @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 사용자 ID", required = true)
      UUID userId
  );

  @Operation(summary = "전체 사용자 조회", description = "모든 사용자를 조회합니다.")
  ResponseEntity<List<UserDto>> getAll();

  @Operation(summary = "사용자 상태 업데이트", description = "사용자의 온라인 상태를 업데이트합니다.")
  ResponseEntity<UserStatus> updateUserStatus(
      @Parameter(description = "상태를 변경할 사용자 ID", required = true)
      UUID userId,

      @Parameter(description = "사용자 상태 업데이트 요청 정보", required = true)
      UserStatusUpdateRequest request
  );
}
