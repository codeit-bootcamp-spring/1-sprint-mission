package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RoleChangeDetectionFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;

  // 권한이 변경되는 경우 401 반환
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
      UUID userId = userDetails.getUser().getId();

      Role currentDbRole = userRepository.findById(userId)
          .map(User::getRole)
          .orElse(null);

      Iterator<? extends GrantedAuthority> iter = userDetails.getAuthorities().iterator();
      Role roleInSession = null;

      if (iter.hasNext()) {
        roleInSession = Role.valueOf(iter.next().getAuthority());
      }

      if (roleInSession != null && currentDbRole != roleInSession) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    filterChain.doFilter(request, response);  // 필터 역할 끝난 후 다음 필터로 요청과 응답 넘김
  }
}
