package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.service.basic.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoDto> login(@RequestBody LoginDto loginDto, HttpSession session) {
        UserInfoDto login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }
}
