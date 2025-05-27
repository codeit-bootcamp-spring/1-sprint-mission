package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping(path = "login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Starting user login: username={}", loginRequest.username());

        UserDto user = authService.login(loginRequest);

        log.info("Completed user login: userId={}, username={}", user.id(), user.username());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(user);
    }

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
}
