package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.handler.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.handler.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final RememberMeServices rememberMeServices;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final JwtService jwtService;
    private final UserValidator userValidator;
    private final JwtSessionRepository jwtSessionRepository;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
        RegisterSessionAuthenticationStrategy sessionAuthenticationStrategy,
        RememberMeServices rememberMeServices, JwtService jwtService, ObjectMapper objectMapper,
        UserValidator userValidator, JwtSessionRepository jwtSessionRepository) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.rememberMeServices = rememberMeServices;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.userValidator = userValidator;
        this.jwtSessionRepository = jwtSessionRepository;
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper, jwtService));
        setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
        setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
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

            User user = userValidator.validateUserExistsByUserName(username);
            jwtSessionRepository.findByUserId(user.getId()).ifPresent(
                value -> {
                    throw new RuntimeException("이미 로그인한 사용자입니다.");
                }
            );

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

            setDetails(request, authRequest);

            rememberMeServices.loginSuccess(request, response, authRequest);

            return getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("Invalid login request", e);
        }
    }
}
