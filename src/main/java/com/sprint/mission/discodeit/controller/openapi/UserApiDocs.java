package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "User API", description = "User 작업")
public interface UserApiDocs {

  @Operation(summary = "User 조회", description = "Find single user using userId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User of userId found"),
      @ApiResponse(
          responseCode = "404",
          description = "No such user of userId found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<UserResponseDto> getUser(
      @Parameter(description = "User ID", required = true)
      String id
  );

  @Operation(summary = "User 등록", description = "새로운 사용자를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User Created"),
      @ApiResponse(
          responseCode = "400",
          description = "User with same email/username exists",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<UserResponseDto> createUser(

      @Parameter(
          description = "User 생성 정보 (JSON)",
          required = true
      )
      CreateUserRequest createUserRequest,

      @Parameter(
          description = "User 프로필 이미지 (binary)",
          required = false,
          content = @Content(
              mediaType = "image/png",
              schema = @Schema(type = "string", format = "binary"
              )
          )
      )
      MultipartFile profile
  );


  @Operation(summary = "모든 User 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "모든 User 조회 성공")
  })
  ResponseEntity<List<UserResponseDto>> getUsers();

  @Operation(summary = "유저 정보 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "유저 정보 업데이트 성공"),
      @ApiResponse(responseCode = "400", description = "중복된 email / username", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "유저를 찾지 못함", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<UserResponseDto> updateUser(

      @Parameter(description = "User UUID", required = true) String id,
      @Parameter(description = "User 프로필 이미지") MultipartFile profile,
      @Parameter(description = "User 업데이트 정보 (JSON)") UserUpdateDto updateDto
  );

  @Operation(summary = "User 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Void> deleteUser(@Parameter(description = "삭제할 User UUID") UUID id);


}
