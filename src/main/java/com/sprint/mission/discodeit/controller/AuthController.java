package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO) {
    User user = authService.login(loginDTO);
    return ResponseEntity.ok(user);
  }
}
