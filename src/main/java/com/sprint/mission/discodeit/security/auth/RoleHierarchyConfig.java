package com.sprint.mission.discodeit.security.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

  @Bean
  public RoleHierarchy roleHierarchy() {
    String hierarchyString = """
            ROLE_ADMIN > ROLE_CHANNEL_MANAGER
            ROLE_CHANNEL_MANAGER > ROLE_USER
        """;
    return RoleHierarchyImpl.fromHierarchy(hierarchyString);
  }
}
