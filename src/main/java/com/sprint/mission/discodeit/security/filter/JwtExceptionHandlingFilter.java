package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.execption.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 가장 먼저 실행
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private static final String JWT_EXCEPTION_ATTRIBUTE = "jwtException";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

            // JWT 예외가 Request Attribute에 설정되어 있으면 처리
            Object jwtError = request.getAttribute(JWT_EXCEPTION_ATTRIBUTE);
            if (jwtError instanceof JwtAuthenticationFilter.JwtErrorType
                && !response.isCommitted()) {
                handleJwtException(response, (JwtAuthenticationFilter.JwtErrorType) jwtError);
            }

        } catch (AccessDeniedException e) {
            handleAccessDeniedException(response, e);
        } catch (AuthenticationException e) {
            handleAuthenticationException(response, e);
        } catch (Exception e) {
            handleGenericException(response, e);
        }
    }

    /**
     * JWT 관련 예외 처리
     */
    private void handleJwtException(HttpServletResponse response,
        JwtAuthenticationFilter.JwtErrorType errorType)
        throws IOException {

        ErrorResponse errorResponse;

        switch (errorType) {
            case EXPIRED_TOKEN:
                errorResponse = ErrorResponse.of(440, "LOGIN_TIMEOUT", "JWT token has expired");
                response.setStatus(440); // Login Timeout (비표준이나 현업 사용됨)
                break;
            case INVALID_SIGNATURE:
                errorResponse = ErrorResponse.of(401, "INVALID_JWT_SIGNATURE",
                    "JWT signature is invalid");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                break;
            case MALFORMED_TOKEN:
                errorResponse = ErrorResponse.of(401, "MALFORMED_JWT", "JWT token is malformed");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                break;
            case INVALID_TOKEN:
            default:
                errorResponse = ErrorResponse.of(401, "INVALID_JWT", "JWT token is invalid");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                break;
        }

        writeErrorResponse(response, errorResponse);
    }

    /**
     * 접근 권한 부족 예외 처리 (403)
     */
    private void handleAccessDeniedException(HttpServletResponse response, AccessDeniedException e)
        throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(
            403,
            "ACCESS_DENIED",
            "Access is denied"
        );
        response.setStatus(HttpStatus.FORBIDDEN.value());
        writeErrorResponse(response, errorResponse);
    }

    /**
     * 인증 실패 예외 처리 (401)
     */
    private void handleAuthenticationException(HttpServletResponse response,
        AuthenticationException e)
        throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(
            401,
            "AUTHENTICATION_FAILED",
            "Authentication failed"
        );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeErrorResponse(response, errorResponse);
    }

    /**
     * 기타 예외 처리 (500)
     */
    private void handleGenericException(HttpServletResponse response, Exception e)
        throws IOException {

        ErrorResponse errorResponse = ErrorResponse.of(
            500,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred"
        );
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        writeErrorResponse(response, errorResponse);
    }

    /**
     * JSON 오류 응답 작성
     */
    private void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
