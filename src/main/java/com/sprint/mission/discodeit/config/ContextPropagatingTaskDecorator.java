package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.springframework.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
@Slf4j
public class ContextPropagatingTaskDecorator implements TaskDecorator {


  @Override
  public Runnable decorate(Runnable runnable) {
    log.info("ContextPropagatingTaskDecorator 동작 중, 현재 스레드 : {}", Thread.currentThread().getName());

    // 1. 현재 스레드에서 Context(MDC, SecurityContextHolder) 캡처
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();
    SecurityContext securityContext = SecurityContextHolder.getContext();
    RequestAttributes requestAttributes =
        RequestContextHolder.getRequestAttributes(); // HTTP 요청 정보 저장

    // 2. 래핑된 Runnable 반환
    return () -> {
      // 이전 컨텍스트 백업
      Map<String, String> previousMdc = MDC.getCopyOfContextMap();
      SecurityContext previousSecurity = SecurityContextHolder.getContext();
      RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
      try {
        // 3. 캡처한 context 복원 - 메인 스레드에서 캡처한 context -> 워커 스레드 threadLocal에 설정
        if (mdcContext != null) {
          MDC.setContextMap(mdcContext);
        }
        SecurityContextHolder.setContext(securityContext);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // 4 . 실제 작업 실행
        runnable.run();
      } finally {
        // 5. 컨텍스트 정리
        // 이전 컨텍스트로 복원
        if (previousMdc != null) {
          MDC.setContextMap(previousMdc);
        } else {
          MDC.clear();
        }
        SecurityContextHolder.setContext(previousSecurity);
        RequestContextHolder.setRequestAttributes(previousRequestAttributes, true);
      }
    };
  }
}
