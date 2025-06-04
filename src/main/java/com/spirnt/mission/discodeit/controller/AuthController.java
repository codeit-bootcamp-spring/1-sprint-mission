package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.AuthApiDocs;
import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.security.jwt.JwtService;
import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import com.spirnt.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMe(@CookieValue(value = "REFRESH-TOKEN") Cookie refreshToken) {
        String accessToken = authService.getMe(refreshToken.getValue());
        return ResponseEntity.ok(accessToken);
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 최소 ROLE_ADMIN 권한 필요
    public ResponseEntity<UserDto> updateRole(
        @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
        UserDto userDto = authService.updateRole(userRoleUpdateRequest);
        jwtService.deleteJwtSession(userRoleUpdateRequest.userId());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
        @CookieValue(value = "REFRESH-TOKEN") Cookie refreshToken, HttpServletResponse response) {
        JwtSession newJwtSession = jwtService.rotateToken(refreshToken.getValue());
        Cookie newCookie = new Cookie("REFRESH-TOKEN", newJwtSession.getRefreshToken());
        newCookie.setHttpOnly(true);
        newCookie.setPath("/");
        response.addCookie(newCookie);
        return ResponseEntity.ok(newJwtSession.getAccessToken());
    }

}
