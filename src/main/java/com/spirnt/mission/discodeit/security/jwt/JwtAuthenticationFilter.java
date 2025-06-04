package com.spirnt.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.exception.auth.InvalidJwtTokenException;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더에서 access token 추출
        String token = extractTokenFromHeader(request);

        // 토큰 유효성 검증
        if (token != null && jwtService.isValid(token)) {
            UUID userId = jwtService.parse(token).userDto().getId();
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                responseUnauthorized(response, new UserNotFoundException(Map.of("userId", userId)));
            }

            // 유효한 토큰이면 인증 정보 설정
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("JWT Authentication Success: {}", userDetails.getUsername());
        } else {
            log.info("JWT Token Validation Failed");
            responseUnauthorized(response, new InvalidJwtTokenException(Map.of("token", token)));
        }
        filterChain.doFilter(request, response);
    }

    // 필터를 거치지 않는 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return (
            path.equals("/") ||
                path.equals("/favicon.ico") ||
                path.equals("/index.html") ||
                path.startsWith("/static/") ||
                path.startsWith("/assets/") ||
                path.startsWith("/error/") ||
                path.startsWith("/actuator/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                (method.equals("POST") && (path.equals("/api/users") || path.equals(
                    "/api/auth/login"))) ||
                path.equals("/api/auth/csrf-token")
        );
    }

    private void responseUnauthorized(HttpServletResponse response, Exception e)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse("JWT_EXCEPTION", e.getMessage(), Map.of(),
            e.getClass().getSimpleName(), 401);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        // AUTHORIZATION 헤더에서 추출
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        // "Bearer " 제거
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
