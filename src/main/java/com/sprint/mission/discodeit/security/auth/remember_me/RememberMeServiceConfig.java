package com.sprint.mission.discodeit.security.auth.remember_me;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
@Configuration
public class RememberMeServiceConfig {


  @Bean
  public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
      PersistentTokenRepository persistentTokenRepository) {
    PersistentTokenBasedRememberMeServices service = new PersistentTokenBasedRememberMeServices(
        "KEY", userDetailsService, persistentTokenRepository);
    service.setTokenValiditySeconds(60 * 60 * 24 * 21);
    service.setParameter("remember-me");

    return service;
  }

}
