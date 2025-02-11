package com.sprint.mission.aop.log;


import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

@Getter
public class TraceStatus {

    private TraceId traceId;
    private Long startTime;
    private ProceedingJoinPoint joinPoint;

    public TraceStatus(TraceId traceId, Long startTime, ProceedingJoinPoint joinPoint) {
        this.traceId = traceId;
        this.startTime = startTime;
        this.joinPoint = joinPoint;
    }
}
