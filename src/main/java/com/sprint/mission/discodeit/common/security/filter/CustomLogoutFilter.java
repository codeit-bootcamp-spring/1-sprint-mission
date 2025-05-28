package com.sprint.mission.discodeit.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private static final String LOGOUT_URI = "/api/auth/logout";

    private final PersistentTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        // 로그아웃 요청만 처리
        if (request.getRequestURI().equals(LOGOUT_URI) && request.getMethod().equals("POST")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 세션 무효화
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }

            // remember-me 토큰 삭제
            if (authentication != null && authentication.getName() != null) {
                tokenRepository.removeUserTokens(authentication.getName());
            }

            // 클라이언트 remember-me 쿠키 삭제
            Cookie cookie = new Cookie("remember-me", null);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 즉시 만료
            response.addCookie(cookie);

            // SecurityContext 초기화
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_OK);
            return; // 다음 필터로 진행하지 않음 (로그아웃 완료)
        }

        // 로그아웃 외의 요청은 그대로 진행
        filterChain.doFilter(request, response);
    }
}

