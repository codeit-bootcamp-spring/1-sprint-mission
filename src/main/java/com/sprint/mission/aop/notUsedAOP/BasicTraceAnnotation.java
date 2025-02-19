package com.sprint.mission.aop.notUsedAOP;


import com.sprint.mission.aop.log.TraceDevice;
import com.sprint.mission.aop.log.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
@RequiredArgsConstructor
//@Component <<<<<<<<<<<<<<<<<<<<<<<<<<<<
// 쓰고 싶을 때 컴포넌트 설정
public class BasicTraceAnnotation {

    private final TraceDevice trace;

    @Pointcut("@within(com.sprint.mission.aop.notUsedAOP.annotation.TraceAnnotation)")
    public void classWithin(){}
    @Pointcut("@within(com.sprint.mission.aop.notUsedAOP.annotation.TraceAnnotation)")
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
