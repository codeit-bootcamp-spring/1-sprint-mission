package com.sprint.mission.aop;


import com.sprint.mission.aop.annotation.TraceAnnotation;
import com.sprint.mission.aop.annotation.ValidTraceAnnotation;
import com.sprint.mission.log.TraceDevice;
import com.sprint.mission.log.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@Aspect
@RequiredArgsConstructor
public class BasicTraceAnnotation {

    private final TraceDevice trace;

    @Pointcut("@within(com.sprint.mission.aop.annotation.TraceAnnotation)")
    public void classWithin(){}

    @Pointcut("@within(com.sprint.mission.aop.annotation.TraceAnnotation)")
    public void methodWith(){}

    @Around("classWithin()")
    public Object doTrace(ProceedingJoinPoint joinPoint){

        TraceStatus status = null;
        try {
            status = trace.begin(joinPoint);
            Object result = joinPoint.proceed();
            trace.end(status, result);
            return result;
        } catch (Throwable e) {
            trace.exception(status, e, null);
            throw new RuntimeException(e);
        }
    }
}
