package com.sprint.mission.discodeit.controller.legacy;

import com.sprint.mission.discodeit.entity.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.user.UserLoginResponse;
import com.sprint.mission.discodeit.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

  private final AuthService authService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public UserLoginResponse login(@Validated @RequestBody UserLoginRequest request) {
    return authService.login(request);
  }
}
