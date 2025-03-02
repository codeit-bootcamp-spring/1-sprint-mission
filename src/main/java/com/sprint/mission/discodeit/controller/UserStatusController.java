package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userStatus")
@Tag(name = "User Status", description = "사용자 상태 관련 정보")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PutMapping("/updateOnline")
  @Operation(
      summary = "사용자 온라인 상태 업데이트",
      description = "특정 사용자의 온라인 상태를 업데이트합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 상태 업데이트 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserStatusResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "사용자 상태를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "UserStatus with userId {userId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserStatusResponse> updateOnlineStatus(
      @Parameter(description = "상태를 업데이트할 사용자 ID")
      @RequestParam("userId") UUID userId) {
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    if (updatedUserStatus == null) {
      throw new NoSuchElementException("UserStatus with userId " + userId + " not found");
    }
    UserStatusResponse response = UserStatusResponse.from(updatedUserStatus);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }

  @GetMapping("/check")
  @Operation(
      summary = "사용자 상태 확인",
      description = "특정 사용자의 현재 상태를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 상태 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserStatusResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "사용자 상태를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "UserStatus with userId {userId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserStatusResponse> checkUserStatus(
      @Parameter(description = "상태를 확인할 사용자 ID")
      @RequestParam("userId") UUID userId) {
    UserStatus userStatus = userStatusService.findByUserId(userId);
    if (userStatus == null) {
      throw new NoSuchElementException("UserStatus with userId " + userId + " not found");
    }
    UserStatusResponse response = UserStatusResponse.from(userStatus);
    return ResponseEntity.ok(response);
  }
}