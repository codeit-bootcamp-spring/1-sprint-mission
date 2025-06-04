package com.sprint.mission.discodeit.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = principal.getUserDto();

        String accessToken = jwtService.generateAccessToken(userDto);
        String refreshToken = jwtService.generateRefreshToken(userDto);

        // Refresh Token -> HttpOnly 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/"); // 모든 경로에 쿠키 적용
        refreshTokenCookie.setMaxAge(
            (int) jwtService.getExpiration(refreshToken).getTime()); // 초 단위
        response.addCookie(refreshTokenCookie);

        // Access Token -> 문자열로 응답 바디에 출력
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(accessToken);

        jwtService.saveJwtSession(userDto, accessToken, refreshToken);
    }
}