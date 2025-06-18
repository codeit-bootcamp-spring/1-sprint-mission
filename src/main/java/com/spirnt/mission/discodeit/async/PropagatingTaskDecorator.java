package com.spirnt.mission.discodeit.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 비동기 스레드에서 MDC 로그 컨텍스트 및 Spring Security 인증 정보(SecurityContext)를 전파하기 위한 클래스
 */
@Component
public class PropagatingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 스레드의 MDC 값 복사
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        // 현재 스레드의 인증 정보 복사
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return () -> {
            // 복사한 정보 붙여넣기
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            try {
                // 원래 비즈니스 로직 실행
                runnable.run();
            } finally {
                // 메모리 누수 및 오염 방지 클리어
                MDC.clear();
                SecurityContextHolder.clearContext();
            }
        };
    }
}
