package com.spirnt.mission.discodeit.controller.swagger;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserApiDocs {

  // 모든 사용자 조회
  @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
  })
  ResponseEntity<List<UserDto>> getAllUsers();

  // 사용자 등록
  @Operation(summary = "User 등록", operationId = "create")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "User with email {email} already exists")))
  })
  ResponseEntity<UserDto> createUser(@RequestPart UserCreateRequest userCreateRequest,
      @Parameter(description = "User 프로필 이미지") @RequestPart(required = false) MultipartFile profile);

  // 사용자 정보 수정
  @Operation(summary = "User 정보 수정", operationId = "update")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "user with email {newEmail} already exists"))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "User with id {userId} not found")))
  })
  ResponseEntity<UserDto> updateUser(
      @Parameter(name = "userId",
          in = ParameterIn.PATH,
          description = "수정할 User ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")) @PathVariable UUID userId,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @Parameter(description = "수정할 User 프로필 이미지") @RequestPart(required = false) MultipartFile profile);

  // 사용자 삭제
  @Operation(summary = "User 삭제", operationId = "delete")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "User with id {id} not found")))
  })
  ResponseEntity<Void> deleteUser(
      @Parameter(name = "userId", description = "삭제할 User ID") @PathVariable UUID userId);

  // 사용자의 온라인 상태 업데이트
  @Operation(summary = "User 온라인 상태 업데이트", operationId = "updateUserStatusByUserId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(schema = @Schema(implementation = UserStatusDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "UserStatus with userId {usreId} not found")))
  })
  ResponseEntity<UserStatusDto> updateUserStatus(
      @Parameter(name = "userId", description = "상태를 변경할 User ID") @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest);
}
