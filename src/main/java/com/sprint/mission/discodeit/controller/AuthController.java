package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthLoginService authLoginService;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody AuthLoginRequest authLoginRequest) {
    UserDto userDto = authLoginService.login(authLoginRequest);
    return ResponseEntity.ok(userDto);
  }

}
