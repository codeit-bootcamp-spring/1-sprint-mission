package com.sprint.mission.discodeit.aspect;

import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class UserStatusAspect {
    private final UserStatusService userStatusService;

    @Before("@annotation(UpdateUserStatus)")
    public void updateUserStatus(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length > 0 && args[0] instanceof UUID userId) {
            userStatusService.updateByUserId(userId);
        }
    }
}
