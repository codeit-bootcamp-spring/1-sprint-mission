package com.sprint.mission;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.servlet.multipart")
public class MultiTypeCheck {
    private boolean enabled;  // "spring.servlet.multipart.enabled" 값을 매핑

    @PostConstruct
    public void init() {
        System.out.println("🚀 Multipart Enabled: " + enabled);
    }
}