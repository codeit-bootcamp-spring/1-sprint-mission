package com.sprint.mission.discodeit.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SecurityMatchers {

  public static final RequestMatcher NON_API = new NegatedRequestMatcher(
      new AntPathRequestMatcher("/api/**"));
  public static final RequestMatcher GET_CSRF_TOKEN = new AntPathRequestMatcher(
      "/api/auth/csrf-token", HttpMethod.GET.name());
  public static final RequestMatcher SIGN_UP = new AntPathRequestMatcher(
      "/api/users", HttpMethod.POST.name());
  public static final RequestMatcher LOGIN = new AntPathRequestMatcher(
      "/api/auth/login", HttpMethod.POST.name());
  public static final String LOGIN_URL = "/api/auth/login";
  public static final RequestMatcher LOGOUT = new AntPathRequestMatcher(
      "/api/auth/logout", HttpMethod.POST.name());
  public static final RequestMatcher ME = new AntPathRequestMatcher(
      "/api/auth/me", HttpMethod.GET.name()
  );
  public static final RequestMatcher REFRESH = new AntPathRequestMatcher(
      "/api/auth/refresh", HttpMethod.POST.name()
  );
  public static final RequestMatcher DOWNLOAD = new AntPathRequestMatcher(
      "/api/binaryContents/*/download", HttpMethod.GET.name()
  );

  public static final RequestMatcher[] PUBLIC_MATCHERS = new RequestMatcher[]{
      NON_API, GET_CSRF_TOKEN, SIGN_UP, LOGIN, LOGOUT, ME, REFRESH, DOWNLOAD
  };

}
