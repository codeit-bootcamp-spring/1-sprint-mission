package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.security.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청마다 실행

    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_EXCEPTION_ATTRIBUTE = "jwtException";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter active: {}", request.getRequestURI());

        // Authorization 헤더에서 JWT 토큰 추출
        String token = extractTokenFromRequest(request);

        try {
            if (token != null && jwtService.validateToken(token)) {

                String username = jwtService.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

                // 인증 객체 생성
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT 인증 성공: {}", authentication.getName());
            } else {
                // 무효한 토큰이면 예외 정보를 Request Attribute에 저장
                request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.INVALID_TOKEN);
                log.debug("JWT 토큰 검증 실패");
            }

        } catch (ExpiredJwtException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.EXPIRED_TOKEN);
            log.debug("JWT 토큰 만료: {}", e.getMessage());
        } catch (SecurityException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.INVALID_SIGNATURE);
            log.debug("JWT 서명 오류: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.MALFORMED_TOKEN);
            log.debug("JWT 형식 오류: {}", e.getMessage());
        } catch (Exception e) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, JwtErrorType.UNKNOWN_ERROR);
            log.error("JWT 처리 중 예상치 못한 오류", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/api/auth/") ||
            !path.startsWith("/api/");
    }

    // Request에서 JWT 토큰 추출
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * TODO : JWT 오류 타입 열거형 -> 수정 예정
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
