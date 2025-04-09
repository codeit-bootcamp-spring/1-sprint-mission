package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.ApiResponse;
import com.sprint.mission.discodeit.dto.response.StatusResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @Operation(summary = "로그인", description = "로그인")
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse<UserDto>> login(
      @RequestBody LoginRequest loginRequest,
      HttpSession session) {

    UserDto userDTO = authService.login(loginRequest);

    // 세션에 사용자 ID 저장
    session.setAttribute("userId", userDTO.getId().toString());

    // 사용자 상태 업데이트
    userService.updateOnlineStatus(userDTO.getId(), true);

    return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", userDTO));
  }

  @Operation(summary = "로그아웃", description = "로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
    String userId = (String) session.getAttribute("userId");

    if (userId != null) {
      // 사용자 상태 업데이트
      userService.updateOnlineStatus(UUID.fromString(userId), false);

      // 세션 무효화
      session.invalidate();

      return ResponseEntity.ok(new ApiResponse<>(true, "로그아웃 성공"));
    } else {
      return ResponseEntity.ok(new ApiResponse<>(false, "로그인 상태가 아닙니다."));
    }
  }

  @Operation(summary = "상태 확인", description = "로그인/로그아웃 상태 확인")
  @GetMapping("/status")
  public ResponseEntity<ApiResponse<StatusResponseDto>> checkLoginStatus(HttpSession session) {
    String userId = (String) session.getAttribute("userId");

    StatusResponseDto statusResponse = StatusResponseDto.builder()
        .loggedIn(userId != null)
        .userId(userId)
        .build();

    return ResponseEntity.ok(new ApiResponse<>(true, "상태 조회 성공", statusResponse));
  }
}
