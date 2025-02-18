package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public UserResponse login(@RequestBody UserRequest.Login userRequestLogin) {
        return authService.login(userRequestLogin);
    }
}
