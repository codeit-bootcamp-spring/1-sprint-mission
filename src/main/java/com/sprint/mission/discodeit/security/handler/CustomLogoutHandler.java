package com.sprint.mission.discodeit.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final PersistentTokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        // 세션 무효화
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        // 컨텍스트 초기화
        SecurityContextHolder.clearContext();

        // remember-me 토큰 제거
        if (authentication != null && authentication.getName() != null) {
            tokenRepository.removeUserTokens(authentication.getName());
        }

        // remember-me 쿠키 삭제
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}
