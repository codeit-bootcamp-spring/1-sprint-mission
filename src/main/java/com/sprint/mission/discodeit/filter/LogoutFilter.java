package com.sprint.mission.discodeit.filter;

import com.sprint.mission.discodeit.redis.RedisRememberMeTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

  private static final String LOGOUT_URI = "/api/auth/logout";
  private static final String LOGOUT_HTTP_METHOD = "POST";

  private final RedisRememberMeTokenRepository redisRememberMeTokenRepository;
  private final PersistentTokenBasedRememberMeServices rememberMeServices;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails principal) {

      redisRememberMeTokenRepository.removeUserTokens(principal.getUsername());
      rememberMeServices.logout(request, response, authentication);

      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      SecurityContextHolder.clearContext();
    }

  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !(LOGOUT_URI.equals(request.getServletPath()) && LOGOUT_HTTP_METHOD.equals(
        request.getMethod()));
  }
}
