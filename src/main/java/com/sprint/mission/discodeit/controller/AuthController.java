package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody AuthRequestDto request) {
        User user = authService.login(request);
        return ResponseEntity.ok(user);
    }
}
