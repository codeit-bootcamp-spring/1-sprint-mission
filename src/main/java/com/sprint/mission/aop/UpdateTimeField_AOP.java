package com.sprint.mission.aop;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Slf4j @Aspect
@Component
public class UpdateTimeField_AOP {

    @Pointcut("execution(* com.sprint.mission.entity..*set*(..))")
    public void updating(){}

    @Pointcut("execution(* com.sprint.mission.entity..*add*(..))")
    public void add(){}

    @Pointcut("execution(* com.sprint.mission.entity..*remove*(..))")
    public void remove(){}

    @AfterReturning("updating() || add() || remove()")
    public void setTimeLog(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();

        switch (target){

            case User user -> {
                user.setUpdateAt(Instant.now());
                log.info("[AOP] User {} updateAt: {}", user.getName(), user.getUpdateAt());
            }

            case Channel channel -> {
                channel.setUpdatedAt(Instant.now());
                log.info("[AOP] Channel {} updateAt: {}", channel.getName(), channel.getUpdatedAt());
            }

            case Message message -> {
                message.setUpdateAt(Instant.now());
                log.info("[AOP] Message {} updateAt: {}", message.getMessage(), message.getUpdateAt());
            }

            default -> log.info("업데이트 지원하지 않는 객체 타입: {}",target.getClass().getSimpleName());
        }
    }
}
