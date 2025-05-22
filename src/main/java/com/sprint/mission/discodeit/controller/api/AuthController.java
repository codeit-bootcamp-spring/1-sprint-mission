package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.AuthApiDocs;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        // spring security가 자동으로 주입
        return ResponseEntity.ok(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserWithSessionId(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userID");
        if (userId == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponse userResponse = userService.findById(userId);

        return ResponseEntity.ok(userResponse);
    }
}
