package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JsonAuthenticationFilter(String defaultFilterProcessUrl,
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(defaultFilterProcessUrl, "POST"));
        setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        log.debug("Attempting JSON authentication");
        if (!request.getContentType().contains("application/json")) {
            throw new RuntimeException("Content type must be application/json");
        }

        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
            LoginRequest.class);
        log.info("Processing login attempt: username={}", loginRequest.username());

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
            loginRequest.username(),
            loginRequest.password()
        );

        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        log.info("Authentication successful for user: {}", authResult.getName());
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        log.warn("Authentication failed: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
