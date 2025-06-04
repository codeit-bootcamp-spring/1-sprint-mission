package com.sprint.mission.discodeit.security.handler;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        // 1. 쿠키에서 refreshToken 추출
        String refreshToken = extractRefreshTokenFromCookies(request.getCookies());
        if (refreshToken != null && jwtService.validate(refreshToken)) {
            // 2. 토큰 무효화
            jwtService.invalidateRefreshToken(refreshToken);
        }

        // 3. 쿠키 삭제
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setHttpOnly(true);
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(deleteCookie);
    }

    private String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
