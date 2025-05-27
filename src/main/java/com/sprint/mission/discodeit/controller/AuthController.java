package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final UserMapper userMapper;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfTokenDto> getCsrfToken(HttpServletRequest request) {
        log.info("Starting CSRF token request");

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken == null) {
            log.warn("CSRF token not found in request attributes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        CsrfTokenDto csrfTokenDto = new CsrfTokenDto(
            csrfToken.getToken(),
            csrfToken.getHeaderName(),
            csrfToken.getParameterName()
        );

        log.info("CSRF token issued successfully");
        return ResponseEntity.ok(csrfTokenDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        log.info("Starting get current user request");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.warn("No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = userMapper.toDto(userDetails.getUser());

        log.info("Current user retrieved: userId={}, username={}", userDto.id(),
            userDto.username());
        return ResponseEntity.ok(userDto);
    }
}
