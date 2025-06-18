package com.sprint.mission.discodeit.common.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextPropagatingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 쓰레드의 MDC 및 SecurityContext 보관
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        RequestAttributes requestAttributes =
            RequestContextHolder.getRequestAttributes(); // 웹 환경아 아닐 경우 null 가능

        // 2. 래핑된 Runnable 반환
        return () -> {
            // 이전 컨텍스트 백업 (중첩 비동기 호출 대비)
            // 워커 스레드에서도 이미 컨텍스트가 설정되어 있을 수 있음 > 중첩 비동기 호출
            // 현재 상태를 백업해두었다가 작업 완료 후 복원해야 함
            Map<String, String> previousMdc = MDC.getCopyOfContextMap();
            SecurityContext previousSecurity =
                SecurityContextHolder.getContext();
            RequestAttributes previousAttributes =
                RequestContextHolder.getRequestAttributes();

            try {
                // 3. 캡처한 컨텍스트 복원
                // 메인 스레드에서 캡처한 컨텍스트를 워커 스레드의 ThreadLocal에 설정
                if (mdcContext != null) {
                    MDC.setContextMap(mdcContext); // 전체 MDC 맵을 한번에 설정
                }
                SecurityContextHolder.setContext(securityContext);
                RequestContextHolder.setRequestAttributes(
                    requestAttributes, true);

                // 4. 실제 작업 실행
                runnable.run();

            } finally {
                // 5. 컨텍스트 정리 (중요!)
                // 이전 컨텍스트로 복원
                if (previousMdc != null) {
                    MDC.setContextMap(previousMdc);
                } else {
                    MDC.clear();
                }

                SecurityContextHolder.setContext(previousSecurity);
                RequestContextHolder.setRequestAttributes(
                    previousAttributes, true);
            }
        };
    }
}
