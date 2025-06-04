package com.sprint.mission.discodeit.security.filter;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_EXCEPTION_ATTRIBUTE = "jwtException";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Authorization 헤더에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);

            if (token != null) {
                // 2. 토큰 유효성 검증
                if (jwtService.validate(token)) {
                    // 3. 유효한 토큰이면 SecurityContext에 인증 정보 설정
                    Authentication authentication = jwtService.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 4. 무효한 토큰이면 예외 정보를 Request Attribute에 저장
                    request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.INVALID_TOKEN);
                }
            }

        } catch (ExpiredJwtException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.EXPIRED_TOKEN);
        } catch (SecurityException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.MALFORMED_TOKEN);
        } catch (Exception e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.UNKNOWN_ERROR);
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 특정 요청에 대해 필터를 건너뛸지 결정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 정적 리소스, 공개 API는 JWT 필터 건너뛰기
        return path.startsWith("/static/") ||
            path.startsWith("/public/") ||
            path.startsWith("/h2-console/") ||
            path.startsWith("/actuator/health") ||
            path.startsWith("/api/auth/") ||
            path.equals("/api/users") ||
            path.equals("/favicon.ico");
    }

    /**
     * Request에서 JWT 토큰 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * JWT 오류 타입 열거형
     */
    public enum JwtErrorType {
        INVALID_TOKEN("Invalid JWT token"),
        EXPIRED_TOKEN("JWT token has expired"),
        INVALID_SIGNATURE("JWT signature is invalid"),
        MALFORMED_TOKEN("JWT token is malformed"),
        UNKNOWN_ERROR("Unknown JWT error");

        private final String message;

        JwtErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
