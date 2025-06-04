package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.LoginRequest;
import com.sprint.mission.discodeit.security.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtBlacklist;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.security.jwt.TokenPair;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;
    private final RememberMeServices rememberMeServices;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtSessionRepository jwtSessionRepository;
    private final JwtBlacklist jwtBlacklist;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(),
                            loginRequest.password())
            );

            User user = userRepository.findByUsername(loginRequest.username())
                    .orElseThrow(UserNotFoundException::new);

            TokenPair tokenPair = jwtService.generateTokenPair(user);

            Cookie refreshCooke = new Cookie("refresh_token", tokenPair.getRefreshToken());
            refreshCooke.setHttpOnly(true);
            refreshCooke.setSecure(true);
            refreshCooke.setPath("/");
            refreshCooke.setMaxAge(7 * 24 * 60 * 60);

            response.addCookie(refreshCooke);

            return ResponseEntity.ok(tokenPair.getAccessToken());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }


    /*@GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }
        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 올바르지 않습니다.");
        }
        return ResponseEntity.ok(userMapper.toDto(userDetails.getUser()));
    }*/

    @GetMapping("/me")
    public ResponseEntity<String> getAccessTokenFromRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
        }

        Optional<String> accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);

        if (accessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        return ResponseEntity.ok(accessToken.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.badRequest().build();
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        jwtService.invalidateRefreshToken(refreshToken);

        Cookie deleteCookie = new Cookie("refresh_token", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
        }
        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        TokenPair newTokenPair = jwtService.reissueTokenPair(refreshToken);

        Cookie refreshCooke = new Cookie("refresh_token", newTokenPair.getRefreshToken());
        refreshCooke.setHttpOnly(true);
        refreshCooke.setSecure(true);
        refreshCooke.setPath("/");
        refreshCooke.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCooke);

        return ResponseEntity.ok(newTokenPair.getAccessToken());

    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(@RequestBody RoleUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(UserNotFoundException::new);
        user.setRole(request.newRole());
        userRepository.save(user);
        //세션
        //sessionRegistry.getAllSessions(user, false).forEach(SessionInformation::expireNow);

        jwtSessionRepository.findFirstByUserId(user.getId())
                .ifPresent(session -> {
                    Claims claims = jwtService.getClaims(session.getAccessToken());
                    long exp = claims.getExpiration().getTime();
                    jwtBlacklist.blacklist(session.getAccessToken(), exp);
                    jwtSessionRepository.delete(session);
                });

        return ResponseEntity.ok().build();
    }
}
