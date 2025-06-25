package com.sprint.mission.discodeit.filter;

import com.sprint.mission.discodeit.aspect.BinaryContentUploadAspect;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class CurrentUserContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      // 현재 인증된 사용자 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null && authentication.isAuthenticated()) {
        // UserDetails에서 userId 추출 (실제 구현에 맞게 조정 필요)
        String username = authentication.getName();
        try {
          UUID userId = UUID.fromString(username);
          BinaryContentUploadAspect.setCurrentUserId(userId);
          log.debug("현재 사용자 ID 설정: {}", userId);
        } catch (IllegalArgumentException e) {
          log.debug("사용자 ID가 UUID 형식이 아닙니다: {}", username);
        }
      }

      // 다음 필터로 진행
      filterChain.doFilter(request, response);

    } finally {
      // 요청 처리 후 ThreadLocal 정리
      BinaryContentUploadAspect.clearCurrentUserId();
    }
  }
}