package com.sprint.mission.discodeit.aop;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This log is for development only
 */
@Slf4j
@Aspect
@Order(2)
@Component
public class MethodLoggingAspect {

  private static final ThreadLocal<List<String>> methodTrace = ThreadLocal.withInitial(
      ArrayList::new);

  @Around("execution(* com.sprint.mission.discodeit.service..*(..)) " +
      "|| execution(* com.sprint.mission.discodeit.service.facade..*(..))")
  public Object traceMethodCalls(ProceedingJoinPoint joinPoint) throws Throwable {

    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    String methodLink = String.format("%s.%s", className, methodName);

    methodTrace.get().add(methodLink);

    return joinPoint.proceed();
  }

  @After("execution(* com.sprint.mission.discodeit.service.facade..*.*(..))")
  public void logFacadeCompletion() {
    List<String> calledMethods = methodTrace.get();

    for (int i = 0; i < calledMethods.size(); i++) {
      log.info("\t\t[Called Method {}] : {}", i, calledMethods.get(i));
    }

    methodTrace.remove();
  }
}
