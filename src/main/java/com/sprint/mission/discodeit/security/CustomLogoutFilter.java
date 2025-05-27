package com.sprint.mission.discodeit.security;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class CustomLogoutFilter extends LogoutFilter {


  public CustomLogoutFilter(
      LogoutSuccessHandler logoutSuccessHandler,
      LogoutHandler... handlers) {
    super(logoutSuccessHandler, handlers);
  }
  public static CustomLogoutFilter createDefault(){
    CustomLogoutFilter filter = new CustomLogoutFilter(
        new HttpStatusReturningLogoutSuccessHandler(), //200 Void 응답 반환
        new SecurityContextLogoutHandler() //세션을 무효화하고 SecurityContext 를 초기화
    );
    filter.setLogoutRequestMatcher(SecurityMatchers.LOGOUT);
    return filter;
  }
}
