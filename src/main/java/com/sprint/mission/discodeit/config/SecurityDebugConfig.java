package com.sprint.mission.discodeit.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityDebugConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityDebugConfig.class);

    @Autowired
    private FilterChainProxy filterChainProxy;

    @PostConstruct
    public void printSecurityFilters() {
        List<SecurityFilterChain> chains = filterChainProxy.getFilterChains();
        chains.forEach(chain -> {
            log.info("=== Security Filter Chain for: {}", chain);
            chain.getFilters().forEach(filter -> log.info("Filter: {}", filter.getClass().getName()));
        });
    }
}
