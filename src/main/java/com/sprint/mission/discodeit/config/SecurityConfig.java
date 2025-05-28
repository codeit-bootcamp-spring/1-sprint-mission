package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter,JwtService jwtService
  ) throws Exception {

    CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfTokenRepository.setCookieName("XSRF-TOKEN");
    csrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

    http
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              Cookie cookie = new Cookie("refreshToken", null);
              cookie.setMaxAge(0);
              cookie.setPath("/");
              response.addCookie(cookie);

              Cookie[] cookies = request.getCookies();
              if (cookies != null) {
                for (Cookie c : cookies) {
                  if ("refreshToken".equals(c.getName())) {
                    jwtService.invalidateSession(c.getValue());
                    break;
                  }
                }
              }

              response.setStatus(HttpServletResponse.SC_OK);
            })
            .deleteCookies("XSRF-TOKEN", "refresh_token")
        )
        .csrf(csrf -> csrf
            .csrfTokenRepository(csrfTokenRepository)
            .ignoringRequestMatchers("/api/auth/login", "/api/auth/csrf-token", "/api/auth/refresh", "/api/auth/logout")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/", "/index.html", "/favicon.ico", "/error",
                "/css/**", "/js/**", "/images/**",
                "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**",
                "/api/auth/csrf-token", "/api/auth/login", "/api/auth/refresh", "/api/auth/logout"
            ).permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/**").authenticated()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(AbstractHttpConfigurer::disable);

    return http.build();
  }
  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }



  @Bean
  public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);

    try (Connection conn = dataSource.getConnection();
        ResultSet rs = conn.getMetaData().getTables(null, null, "persistent_logins", null)) {

      if (!rs.next()) {
        try (Statement stmt = conn.createStatement()) {
          stmt.executeUpdate("""
                    create table persistent_logins (
                        username varchar(64) not null,
                        series varchar(64) primary key,
                        token varchar(64) not null,
                        last_used timestamp not null
                    )
                """);
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException("생성 안됨", e);
    }

    return repo;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
