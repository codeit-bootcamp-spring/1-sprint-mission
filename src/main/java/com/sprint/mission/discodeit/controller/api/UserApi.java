package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
public interface UserApi {

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "사용자 생성 성공")
  ResponseEntity<UserResponse> createUser(
      CreateUserRequest request,
      MultipartFile file
  )
      throws
      IOException;

  @Operation(summary = "특정 사용자 조회", description = "사용자 ID를 이용하여 특정 사용자를 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  ResponseEntity<UserResponse> getUserById(UUID id);

  @Operation(summary = "전체 사용자 조회", description = "등록된 모든 사용자를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
  ResponseEntity<List<UserResponse>> getAllUsers();

  @Operation(summary = "사용자 삭제", description = "사용자 ID를 이용하여 특정 사용자를 삭제합니다.")
  @ApiResponses({@ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  ResponseEntity<Void> deleteUser(UUID id);

  @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  ResponseEntity<UserResponse> updateUser(
      UUID id,
      UpdateUserRequest request,
      MultipartFile file
  )
      throws
      IOException;

  @Operation(summary = "사용자 접속 상태 수정", description = "사용자의 접속 상태 정보를 수정합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "사용자 접속 정보 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")})
  ResponseEntity<UserStatusResponse> updateUserUserStatus(
      UUID userId,
      UpdateUserStatusRequest request
  );
}
