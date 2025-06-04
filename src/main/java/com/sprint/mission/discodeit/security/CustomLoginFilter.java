package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ErrorResponse;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.TokenPair;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public CustomLoginFilter(AuthenticationManager authenticationManager,
                             ObjectMapper objectMapper,
                             UserMapper userMapper,
                             JwtService jwtService
    ) {
        this.objectMapper = objectMapper;
        super.setAuthenticationManager(authenticationManager);
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/api/auth/login"); // 요청 URL 설정
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequest loginRequest = objectMapper
                    .readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        // 1. 인증된 사용자 정보 > DTO 변환
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        UserDto userDto = userMapper.toDto(userDetails.getUser());

        // 2. JWT 토큰 발급
        TokenPair tokens = jwtService.issueTokenPair(userDto);

        // 3. 응답 본문에 액세스 토큰
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(tokens.accessToken());

        // 4. HttpOnly 쿠키로 리프레시 토큰 저장
        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 21) // 21일
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.from(failed, HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}