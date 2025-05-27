package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        log.warn("Authentication failed: {}", exception.getMessage());

        ErrorResponse errorResponse;

        if (exception instanceof UsernameNotFoundException) {
            errorResponse = ErrorResponse.of(
                Instant.now(),
                ErrorCode.USER_NOT_FOUND.name(),
                exception.getMessage(),
                new HashMap<>(),
                exception.getClass().getSimpleName(),
                HttpStatus.UNAUTHORIZED.value()
            );
        } else if (exception instanceof BadCredentialsException) {
            errorResponse = ErrorResponse.of(
                Instant.now(),
                ErrorCode.INVALID_PASSWORD.name(),
                "Wrong password",
                new HashMap<>(),
                exception.getClass().getSimpleName(),
                HttpStatus.UNAUTHORIZED.value()
            );
        } else {
            errorResponse = ErrorResponse.of(
                Instant.now(),
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                "Authentication failed",
                new HashMap<>(),
                exception.getClass().getSimpleName(),
                HttpStatus.UNAUTHORIZED.value()
            );
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}