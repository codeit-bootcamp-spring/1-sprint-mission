package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  
  @PostMapping
  public ResponseEntity<User> login(@RequestBody AuthUserDTO authUserDTO) {
    User user = authService.isUserExist(authUserDTO);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }

}
