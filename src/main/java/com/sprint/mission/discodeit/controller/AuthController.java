package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsersDto>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                       BindingResult bindingResult,
                                                       HttpSession session) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "입력값이 유효하지 않습니다."));
        }

        try {
            UserDto userDTO = userService.findByEmail(loginRequest.getEmail());

            if (userDTO != null && userDTO.getPassword().equals(loginRequest.getPassword())) {
                session.setAttribute("userId", userDTO.getId().toString());
                userService.updateOnlineStatus(userDTO.getId(), true);

                return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false,"이메일 또는 비밀번호 확인해주세요."));
            }
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {} ", e.getMessage() , e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "로그인 처리 중 오류 발생"));
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<UserDto>> logout(HttpSession session) {

        try {
            String userId = (String) session.getAttribute("userId");

            if (userId != null) {
                userService.updateOnlineStatus(UUID.fromString(userId), false);
                session.invalidate();

                return ResponseEntity.ok(new ApiResponse(true, "로그아웃 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "로그인 상태가 아닙니다."));
            }
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생 : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "로그아웃 처리 중 오류가 발생했습니다."));
        }
    }


    @Operation(summary = "상태 확인", description = "로그인/로그아웃 상태 확인")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<StatusResponseDto>> checkLoginStatus(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        StatusResponseDto statusResponse;
        if (userId != null) {
            statusResponse = StatusResponseDto.builder()
                    .loggedIn(true)
                    .userId(userId)
                    .build();

        } else {
            statusResponse = StatusResponseDto.builder()
                    .loggedIn(false)
                    .build();
        }

        ApiResponse<StatusResponseDto> response = new ApiResponse<>(true, "Status retrieved");
        return ResponseEntity.ok(response);
    }
}