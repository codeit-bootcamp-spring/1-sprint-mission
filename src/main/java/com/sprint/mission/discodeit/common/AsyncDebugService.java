package com.sprint.mission.discodeit.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncDebugService {

  @Async("unifiedPool")
  public CompletableFuture<Map<String, String>> checkContext() {
    String requestId = MDC.get("requestId");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    log.info("Async Thread - thread: {}", Thread.currentThread().getName());
    log.info("Async Thread - requestId: {}", requestId);
    log.info("Async Thread - username: {}", auth != null ? auth.getName() : "null");

    Map<String, String> result = new HashMap<>();
    result.put("requestId", requestId);
    result.put("username", auth != null ? auth.getName() : "null");

    return CompletableFuture.completedFuture(result);
  }

}
