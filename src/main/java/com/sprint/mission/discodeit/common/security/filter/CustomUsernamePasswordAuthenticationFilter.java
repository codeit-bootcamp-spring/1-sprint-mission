package com.sprint.mission.discodeit.common.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.execption.ErrorCode;
import com.sprint.mission.discodeit.execption.ErrorResponse;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserMapper userMapper;
    private final HttpSessionSecurityContextRepository contextRepository;
    private final RegisterSessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final SessionRegistry sessionRegistry;
    private final RememberMeServices rememberMeServices;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
        UserMapper userMapper,
        HttpSessionSecurityContextRepository httpSessionSecurityContextRepository,
        RegisterSessionAuthenticationStrategy sessionAuthenticationStrategy,
        SessionRegistry sessionRegistry, RememberMeServices rememberMeServices) {
        this.userMapper = userMapper;
        this.contextRepository = httpSessionSecurityContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.sessionRegistry = sessionRegistry;
        this.rememberMeServices = rememberMeServices;
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login"); // 요청 경로 지정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
        }
        try {
            Map<String, String> credentials = new ObjectMapper()
                .readValue(request.getInputStream(), new TypeReference<>() {
                });
            String username = credentials.get("username");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

            super.setDetails(request, authRequest);

            rememberMeServices.loginSuccess(request, response, authRequest);

            return super.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("Invalid login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal(); // UserDetails 구현체
        User user = customUserDetails.getUser();

        boolean online = false;
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (Objects.equals(((UserDetails) principal).getUsername(), user.getName())) {
                online = true;
            }
        }

        UserDto userDto = userMapper.toUserDto(user, online);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), userDto);

        // 1. SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // 2. 세션 저장을 위한 SecurityContextRepository 사용
        contextRepository.saveContext(context, request, response);

        sessionAuthenticationStrategy.onAuthentication(authResult, request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);

        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            ErrorCode.UNAUTHORIZED.getCode(),
            ErrorCode.UNAUTHORIZED.getMessage(),
            new HashMap<>(),
            failed.getClass().getSimpleName(),
            HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

