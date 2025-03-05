package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  @PostMapping(value = "/login")
  public ResponseEntity<User> login(@RequestBody AuthRequestDto request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));

  }
}
