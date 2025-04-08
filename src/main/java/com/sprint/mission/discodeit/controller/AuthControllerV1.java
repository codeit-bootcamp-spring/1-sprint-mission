package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping(value = "/login")
  public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request) {
    authService.login(request);
    UserDto userDTO = userService.find(request.userId());
    return ResponseEntity.status(HttpStatus.OK).body(userDTO);
  }
}
