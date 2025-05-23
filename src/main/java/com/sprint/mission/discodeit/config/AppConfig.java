package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableJpaAuditing
public class AppConfig {


  @Bean
  UserDetailsService uds(PasswordEncoder enc) {
    UserDetails u = User.withUsername("api-user")
        .password(enc.encode("p@ssw0rd")) //BCrypt 암호화
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(u);
  }

  @Bean
  PasswordEncoder enc() {
    return new BCryptPasswordEncoder();
  }
}
