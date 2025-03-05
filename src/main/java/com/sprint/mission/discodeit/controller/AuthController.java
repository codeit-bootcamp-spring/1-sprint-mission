package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   BindingResult bindingResult,
                                   HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("message", "입력값이 유효하지 않습니다");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            UserDto userDTO = userService.find(loginRequest.getEmail());

            if (userDTO != null && userDTO.getPassword().equals(loginRequest.getPassword())) {
                session.setAttribute("userId", userDTO.getId().toString());
                userService.updateOnlineStatus(userDTO.getId().toString(), true);

                response.put("success", true);
                response.put("userId", userDTO.getId());
                response.put("message", "로그인 성공");

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "이메일 또는 비밀번호를 확인해주세요");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "로그인 처리 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            String userId = (String) session.getAttribute("userId");

            if (userId != null) {
                userService.updateOnlineStatus(userId, false);
                session.invalidate();

                response.put("success", true);
                response.put("message", "로그아웃 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "로그인 상태가 아닙니다");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "로그아웃 처리 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "상태 확인", description = "로그인/로그아웃 상태 확인")
    @GetMapping("/status")
    public ResponseEntity<?> checkLoginStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            response.put("loggedIn", true);
            response.put("userId", userId);
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}