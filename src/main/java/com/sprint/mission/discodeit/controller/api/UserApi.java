package com.sprint.mission.discodeit.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserApi {

  @Operation(summary = "User 등록", description = "새로운 사용자를 등록합니다.")
  @ApiResponse(responseCode = "201", description = "User 생성 성공")
  @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자")
  ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") String jsonRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws JsonProcessingException;

  @Operation(summary = "User 정보 수정", description = "사용자의 정보를 수정합니다.")
  ResponseEntity<UserDto> update(@PathVariable("id") UUID id,
      @RequestPart("userUpdateRequest") String jsonRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws JsonProcessingException;

  @Operation(summary = "User 삭제", description = "사용자를 삭제합니다.")
  @ApiResponse(responseCode = "204", description = "User 삭제 성공")
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  ResponseEntity<Void> delete(@PathVariable("id") UUID id);

  @Operation(summary = "전체 User 목록 조회", description = "모든 사용자 정보를 가져옵니다.")
  @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UserDto.class)))
  ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "User 온라인 상태 업데이트", description = "특정 사용자의 온라인 상태를 업데이트합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 온라인 상태 업데이트 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID", required = true) @PathVariable("id") UUID id,
      @RequestPart String jsonRequest) throws JsonProcessingException;


}
