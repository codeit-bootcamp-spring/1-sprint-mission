package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.AuthApiDocs;
import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
    UserDto user = authService.login(loginRequest);
    return ResponseEntity.ok(user);
  }
}
