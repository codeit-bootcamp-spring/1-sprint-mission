package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.user.UserLoginResponse;
import com.sprint.mission.discodeit.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserLoginResponse login(@Validated @RequestBody UserLoginRequest request) {
        return authService.login(request);
    }
}
