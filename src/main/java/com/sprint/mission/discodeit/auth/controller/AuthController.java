package com.sprint.mission.discodeit.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.auth.dto.LoginUserRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인", description = "회원 로그인 API")
	@PostMapping(value = "/login")
	public ResponseEntity<User> login(@RequestBody LoginUserRequest loginRequest) {
		User user = authService.login(loginRequest);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
