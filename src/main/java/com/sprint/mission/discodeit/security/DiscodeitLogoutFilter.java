package com.sprint.mission.discodeit.security;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class DiscodeitLogoutFilter extends LogoutFilter {

  public DiscodeitLogoutFilter(
      LogoutSuccessHandler logoutSuccessHandler,
      LogoutHandler... handlers) {
    super(logoutSuccessHandler, handlers);
  }
}
