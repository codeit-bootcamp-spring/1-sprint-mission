package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextPropagatingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();

        return () -> {

            // 이전 컨텍스트 백업 (중첩 비동기 호출 대비)
            Map<String, String> previousMdc = MDC.getCopyOfContextMap();
            SecurityContext previousSecurity = SecurityContextHolder.getContext();

            try {
                SecurityContextHolder.setContext(securityContext);

                if (mdcContext != null) {
                    MDC.setContextMap(mdcContext);
                }

                runnable.run();

            } finally {

                SecurityContextHolder.setContext(previousSecurity);

                if (previousMdc != null) {
                    MDC.setContextMap(previousMdc);
                } else {
                    MDC.clear();
                }

            }
        };
    }
}
