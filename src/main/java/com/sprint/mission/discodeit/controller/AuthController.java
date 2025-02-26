package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AuthRequestDTO;
import com.sprint.mission.discodeit.dto.AuthResponseDTO;
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
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        Optional<AuthResponseDTO> authResponse = authService.login(authRequestDTO);

        return authResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body(new AuthResponseDTO(null, "로그인 실패")));
    }
}
