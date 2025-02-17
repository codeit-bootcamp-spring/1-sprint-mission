package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    @PostMapping
    public UserResponseDto login(@RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }
}
