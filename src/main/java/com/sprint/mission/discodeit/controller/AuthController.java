package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AuthRequest;
import com.sprint.mission.discodeit.dto.AuthResponse;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ✅ 로그인 API (단순 로그인 방식)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Optional<AuthResponse> authResponse = authService.login(authRequest);

        return authResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body(new AuthResponse(null, "로그인 실패")));
    }
}
