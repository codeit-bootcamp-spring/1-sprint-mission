package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JsonLogoutFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher logoutReqeustMatcher;

    public JsonLogoutFilter(String logoutUrl) {
        this.logoutReqeustMatcher = new AntPathRequestMatcher(logoutUrl, "POST");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (logoutReqeustMatcher.matches(request)) {
            log.info("Processing logout request");

            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if (session != null) {
                log.debug("Invalidating session: {}", session.getId());
                session.invalidate();
            }

            response.setStatus(HttpServletResponse.SC_OK);
            log.info("Logout successful");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
