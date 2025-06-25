package com.sprint.mission.discodeit.security.handler;

import com.sprint.mission.discodeit.event.UserListChangedEvent;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        if (authentication == null
            || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            return;
        }

        resolveRefreshToken(request)
            .ifPresent(refreshToken -> {
                jwtService.revokeRefreshToken(refreshToken);
                invalidateRefreshTokenCookie(response);
            });

        clearUsersCache();

        eventPublisher.publishEvent(new UserListChangedEvent(principal.getId()));
    }

    private Optional<String> resolveRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME))
            .findFirst()
            .map(Cookie::getValue);
    }

    private void invalidateRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
    }

    private void clearUsersCache() {
        Objects.requireNonNull(cacheManager.getCache("users")).clear();
    }
}
