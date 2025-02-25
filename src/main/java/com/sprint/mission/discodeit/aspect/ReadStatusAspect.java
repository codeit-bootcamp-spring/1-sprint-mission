package com.sprint.mission.discodeit.aspect;

import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class ReadStatusAspect {
    private final ReadStatusService readStatusService;

    @Before("@annotation(UpdateReadStatus)")
    public void updateReadStatus(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length > 0 && args[0] instanceof UUID channelId) {
            readStatusService.updateByChannelId(channelId);
        }
    }
}
