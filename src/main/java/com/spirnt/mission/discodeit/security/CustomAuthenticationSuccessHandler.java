package com.spirnt.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.security.jwt.JwtService;
import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        // 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 인증된 사용자 정보
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // DTO 매핑
        User user = userDetails.getUser();
        UserDto userDto = UserDto.from(user, true);

        // 토큰 발급
        // 동시 로그인 제한: 이전 JwtSession을 제거
        jwtService.deleteJwtSession(user.getId());
        JwtSession jwtSession = jwtService.createJwtSession(userDto);
        String accessToken = jwtSession.getAccessToken();

        // response
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 쿠키에 refresh token 추가
        Cookie refreshTokenCookie = new Cookie("REFRESH-TOKEN", jwtSession.getRefreshToken());
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송 (개발 중엔 false 가능)
        refreshTokenCookie.setPath("/");      // 모든 경로에 대해 쿠키 전송
        response.addCookie(refreshTokenCookie);
        objectMapper.writeValue(response.getWriter(), accessToken);
    }

}
