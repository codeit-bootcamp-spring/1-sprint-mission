package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getUserInfo(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(jwtService.getAccessToken(refreshToken));
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(@RequestBody @Valid UserRoleUpdateRequest request) {
        return ResponseEntity.ok(authService.updateUserRole(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
        @CookieValue("refreshToken") String refreshToken,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletResponse response
    ) {
        return ResponseEntity.ok(
            jwtService.refresh(refreshToken, userDetails.getUserDto(), response));
    }
}
