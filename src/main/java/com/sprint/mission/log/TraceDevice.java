package com.sprint.mission.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class TraceDevice {

    private static final String START_PREFIX = "-->";
    private static final String COMPLET_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceHolder = new ThreadLocal<>();

    public TraceStatus begin(ProceedingJoinPoint joinPoint){
        synchronize();

        TraceId traceId = traceHolder.get();
        Long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String simpleClassName = joinPoint.getTarget().getClass().getSimpleName();
        log.info("[{}] {} {}(Method {}, Args {})", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()),simpleClassName, methodName, joinPoint.getArgs());
        return new TraceStatus(traceId, startTime, joinPoint);
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level+1; i++) {
            sb.append((i == level) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

    private void synchronize() {
        TraceId traceId = traceHolder.get();
        if (traceId == null) {
            log.info("======================================================");
            traceHolder.set(new TraceId());
        }
        else traceHolder.set(traceId.createNextId());
    }

    public void end(TraceStatus status){
        complete(status, null);
    }

    public void exception(TraceStatus status, Throwable e){
        complete(status, e);
    }

    private void complete(TraceStatus status, Throwable e) {
        TraceId traceId = traceHolder.get();
        ProceedingJoinPoint joinPoint = status.getJoinPoint();
        //Long resultTime = System.currentTimeMillis() - status.getStartTime();
        String methodName = joinPoint.getSignature().getName();
        String simpleClassName = joinPoint.getTarget().getClass().getSimpleName();

        if (e == null){
            log.info("[{}] {} {}(Method {}, Args {})", traceId.getId(), addSpace(COMPLET_PREFIX, traceId.getLevel()), simpleClassName, methodName);
        } else {
            log.info("[{}] {} {}(Method {}, Args {}) *예외 ={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), simpleClassName, methodName, joinPoint.getArgs(), e.getClass().getSimpleName());
        }
        releaseHolder(status);
    }

    private void releaseHolder(TraceStatus status) {
        TraceId traceId = traceHolder.get();
        if (traceId.getLevel() == 0){
            Long resultTime = System.currentTimeMillis() - status.getStartTime();
            log.info("[{}] 걸린 시간 : {}ms", traceId.getId(), resultTime);
            log.info("======================================================");
            traceHolder.remove();
        } else {
            traceHolder.set(traceId.createPrevId());
        }
    }

}
