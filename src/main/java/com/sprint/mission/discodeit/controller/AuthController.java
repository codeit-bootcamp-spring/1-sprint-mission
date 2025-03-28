package com.sprint.mission.discodeit.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController implements AuthApi {

	private final AuthService authService;

	@PostMapping(path = "login")
	public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
		log.info("Login request - username: {}", loginRequest.username());
		try {
			UserDto user = authService.login(loginRequest);
			log.info("Login successful - username: {}", loginRequest.username());
			return ResponseEntity.ok(user);
		} catch (NoSuchElementException e) {
			log.warn("Login failed - User not found: {}", loginRequest.username());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (IllegalArgumentException e) {
			log.warn("Login failed - Incorrect password: {}", loginRequest.username());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		} catch (Exception e) {
			log.error("Unexpected error during login - username: {}", loginRequest.username(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
