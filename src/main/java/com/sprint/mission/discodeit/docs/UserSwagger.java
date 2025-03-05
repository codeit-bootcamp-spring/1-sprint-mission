package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.response.UserUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserSwagger {

  @Operation(operationId = "create", summary = "User 등록")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함", content = @Content(
          examples = @ExampleObject(value = "User with email {email} already exists")
      ))
  })
  ResponseEntity<UserCreateResponse> create(
      UserCreateRequest request,
      MultipartFile profile
  );

  @Operation(operationId = "update", summary = "User 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함", content = @Content(
          examples = @ExampleObject(value = "user with email {newEmail} already exists")
      )),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "User with id {userId} not found")
      ))
  })
  ResponseEntity<UserUpdateResponse> update(
      UUID userId,
      UserUpdateRequest request,
      MultipartFile profile
  );

  @Operation(operationId = "delete", summary = "User 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "User with id {id} not found")
      ))
  })
  ResponseEntity<Void> delete(UUID userId);

  @Operation(operationId = "findAll", summary = "전체 User 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
  })
  ResponseEntity<List<UserDto>> findAll();

  @Operation(operationId = "updateUserStatusByUserId", summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨"),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
      ))
  })
  ResponseEntity<UserStatusUpdateRequest> updateUserStatus(
      UUID userId,
      UserStatusUpdateRequest request
  );

}