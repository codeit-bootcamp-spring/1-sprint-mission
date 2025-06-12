package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // 메서드 보안 활성화
public class MethodSecurityConfig {

  // @EnableMethodSecurity 를 활성화 해 주어야 @PreAuthorize 와 @PostAuthorize 등 애노테이션을 사용할 수 있음
  // 추가 설정이 필요하면 여기에...
}
