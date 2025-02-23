package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public String login(@RequestBody AuthUserDTO authUserDTO) {
        User user = authService.isUserExist(authUserDTO);
        return "login succeeded";
    }

}
