package com.sprint.mission.aop.usedAOP;


import com.sprint.mission.aop.log.TraceDevice;
import com.sprint.mission.aop.log.TraceStatus;
import com.sprint.mission.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class BasicTrace {

  private final TraceDevice trace;

  @Pointcut("execution(* com.sprint.mission.controller..*(..))")
  public void controllerMethod() {
  }

  @Pointcut("execution(* com.sprint.mission.service.jcf..*(..))")
  public void serviceMethod() {
  }

  @Pointcut("execution(* com.sprint.mission.repository.jcf..*(..))")
  public void repositoryMethod() {
  }

//    @Pointcut("execution(* com.sprint.mission.repository.jcf..create(..))")
//    public void jcfCreateMethod(){} // 테스트 1회성

  @Around("serviceMethod() || repositoryMethod() || controllerMethod()")
  public Object doTrace(ProceedingJoinPoint joinPoint) {

    TraceStatus status = null;
    try {
      status = trace.begin(joinPoint);
      Object result = joinPoint.proceed();
      trace.end(status, result);
      return result;
    } catch (CustomException e) {
      trace.exception(status, e, null);
      throw e;
    } catch (Throwable e) {
      trace.exception(status, e, null);
      throw new RuntimeException(e);
    }
  }
}
