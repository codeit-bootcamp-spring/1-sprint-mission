package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.global.interceptor.MDCLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public MDCLoggingInterceptor mdcLoggingInterceptor() {
        return new MDCLoggingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcLoggingInterceptor())
            .addPathPatterns("/**"); // 모든 경로에 적용
    }
}
