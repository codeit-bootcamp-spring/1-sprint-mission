package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.docs.AuthSwagger;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {

  private final AuthService authService;

  @RequestMapping(value = "/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.POST)
  public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
    User loginedUser = authService.login(loginRequest);

    return ResponseEntity.status(HttpStatus.OK).body(loginedUser);
  }
}
