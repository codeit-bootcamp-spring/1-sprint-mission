package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

  private final AuthService authService;

  @PostMapping(value = "/login")
  public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request) {
    log.info("로그인 요청: username={}", request.userName());
    UserDto login = authService.login(request);
    log.debug("로그인 응답: {}", login);
    return ResponseEntity.status(HttpStatus.OK).body(login);
  }
}
