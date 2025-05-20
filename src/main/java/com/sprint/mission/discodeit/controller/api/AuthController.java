package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.AuthApiDocs;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<UserResponse> login(
        @Valid @RequestBody UserRequest.Login userRequestLogin) {

        log.info("POST /api/login - login attempt for user: {}", userRequestLogin.getUsername());
        return ResponseEntity.ok(authService.login(userRequestLogin));
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        // spring security가 자동으로 주입
        return ResponseEntity.ok(csrfToken);
    }
}
