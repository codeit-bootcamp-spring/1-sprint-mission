package com.sprint.mission.aop;


import com.sprint.mission.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Aspect
public class BasicTrace_AOP {

    // 이거 나중에 C-S-R 단계별 화살표 적용하기
//
//    @Pointcut("execution(* com.sprint.mission.controller.*())")
//    public void controllerMethod(){}

    @Pointcut("execution(* com.sprint.mission.service.*())")
    public void serviceMethod(){}

    @Pointcut("execution(* com.sprint.mission.repository.*())")
    public void repositoryMethod(){}

    @Around("serviceMethod() || repositoryMethod()")
    public Object doTrace(ProceedingJoinPoint joinPoint){

        String methodName = joinPoint.getSignature().getName();
        String simpleClassName = joinPoint.getTarget().getClass().getSimpleName();
        log.info("[{}] {} start", simpleClassName, methodName);
        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long resultTime = System.currentTimeMillis() - startTime;

            log.info("[{}] {} complete(time: {}ms", simpleClassName, methodName, resultTime);
            return result;
        } catch (Throwable e) {
            log.info("[{}] {} exception(cause: {}", simpleClassName, methodName, e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }
}
