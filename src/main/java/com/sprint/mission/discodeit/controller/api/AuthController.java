package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.AuthApiDocs;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.security.CustomUserDetailService;
import com.sprint.mission.discodeit.security.jwt.JwtProperties;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final CustomUserDetailService customUserDetailService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        // spring security가 자동으로 주입
        return ResponseEntity.ok(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(
        @CookieValue(value = JwtService.REFRESH_TOKEN_COOKIE_NAME, required = false) String cookieRefreshToken
    ) {
        if (cookieRefreshToken == null || cookieRefreshToken.isEmpty()) {
            throw new BadCredentialsException("인증 정보가 없습니다");
        }

        JwtSession jwtSession = jwtService.findJwtSessionByRefreshToken(cookieRefreshToken);

        return ResponseEntity.ok(jwtSession.getAccessToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> getAccessTokenByRefreshToken(
        @CookieValue(value = JwtService.REFRESH_TOKEN_COOKIE_NAME, required = false) String cookieRefreshToken,
        HttpServletResponse response
    ) {
        if (cookieRefreshToken == null || cookieRefreshToken.isEmpty()) {
            throw new BadCredentialsException("인증 정보가 없습니다");
        }

        JwtSession jwtSession = jwtService.findJwtSessionByRefreshToken(cookieRefreshToken);
        UserResponse userResponse = userService.findById(jwtSession.getUserId());
        UserDetails userDetails = customUserDetailService.loadUserByUsername(
            userResponse.username());

        String newAccessToken = jwtService.generateAccessToken(userDetails, userResponse);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        jwtService.updateJwtSession(jwtSession, newAccessToken, newRefreshToken);

        addRefreshTokenCookie(response, newRefreshToken);

        return ResponseEntity.ok(newAccessToken);
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponse> updateUserRole(
        @RequestBody UserRoleUpdateRequest request) {

        UserResponse userResponse = authService.changeUserRole(request);
        jwtService.revokeAllUserSessions(request.getUserId());

        return ResponseEntity.ok(userResponse);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtProperties.getRefreshToken().getValiditySeconds());

        response.addCookie(cookie);
    }

}
