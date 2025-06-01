package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/api/auth/logout")
                && request.getMethod().equalsIgnoreCase("POST")) {

            // 1. 쿠키에서 refresh_token 추출
            String refreshToken = extractRefreshToken(request);

            if (refreshToken != null) {
                // 2. 리프레시 토큰 무효화
                jwtService.revoke(refreshToken);
            }

            // 3. 쿠키 삭제
            Cookie expiredCookie = new Cookie("refresh_token", null);
            expiredCookie.setPath("/");
            expiredCookie.setHttpOnly(true);
            expiredCookie.setMaxAge(0); // 즉시 만료
            response.addCookie(expiredCookie);

            // 4. 응답 반환
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
