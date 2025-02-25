package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final BasicAuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam("id") UUID userId, @RequestBody LoginRequest request) {
        UserDTO login = authService.login(userId, request);
        return ResponseEntity.ok(
                "User Id: " + login.getId() +
                        "User Name: " + login.getName() +
                        " 님의 로그인이 완료되었습니다."
        );
    }
}
