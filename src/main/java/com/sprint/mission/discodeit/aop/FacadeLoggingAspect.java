package com.sprint.mission.discodeit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FacadeLoggingAspect {

  @Around("execution(* com.sprint.mission.discodeit.service.facade..*MasterFacade.*(..))")
  public Object logFacadeMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();
    String params = (args.length > 0) ? args[0].toString() : "NO PARAMS";

    log.info("[FACADE] 요청 시작 [{}], [{}]", methodName, params);

    try {
      Object result = joinPoint.proceed();
      log.info("[FACADE] 요청 완료 [{}]", methodName);
      return result;
    } catch (Exception e) {
      log.error("[FACADE] 요청 실패 [{}]", methodName);
      throw e;
    }
  }
}
