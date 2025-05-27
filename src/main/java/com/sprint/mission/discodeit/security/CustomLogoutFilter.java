package com.sprint.mission.discodeit.security;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class CustomLogoutFilter extends LogoutFilter {

  public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
    super(logoutSuccessHandler, handlers);
  }

  public static CustomLogoutFilter createDefault() {
    CustomLogoutFilter filter = new CustomLogoutFilter(
        new HttpStatusReturningLogoutSuccessHandler(),
        new SecurityContextLogoutHandler()
    );
    filter.setLogoutRequestMatcher(SecurityMatchers.LOGOUT);

    return filter;
  }
}