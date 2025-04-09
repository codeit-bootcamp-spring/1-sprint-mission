package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.AuthApiDocs;
import com.sprint.mission.discodeit.dto.auth.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

  private final AuthService authService;

  @Override
  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> userLogin(@Valid @RequestBody LoginDto loginDto) {
    log.debug("[LOGIN REQUEST] : [USERNAME:{}]", loginDto.username());
    UserResponseDto user = authService.login(loginDto.username(), loginDto.password());
    return ResponseEntity.ok(user);
  }
}
