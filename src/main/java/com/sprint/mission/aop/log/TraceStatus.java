package com.sprint.mission.aop.log;


import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

@Getter
public class TraceStatus {

    private final TraceId traceId;
    private final Long startTime;
    private final ProceedingJoinPoint joinPoint;

    public TraceStatus(TraceId traceId, Long startTime, ProceedingJoinPoint joinPoint) {
        this.traceId = traceId;
        this.startTime = startTime;
        this.joinPoint = joinPoint;
    }
}
