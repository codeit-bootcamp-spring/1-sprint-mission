package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.auth.RequestLogin;
import com.sprint.mission.discodeit.entity.auth.ResponseLogin;
import com.sprint.mission.discodeit.service.auth.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthControllerV2 {

  private final UserAuthService userAuthService;

  @PostMapping("/login")
  public ResponseLogin login(@RequestBody @Validated RequestLogin request) {
    return userAuthService.login(request);
  }
}
