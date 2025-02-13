package com.sprint.mission.aop.useAOP;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Slf4j @Aspect
@Component
public class UpdateTimeField_AOP {

//    @Pointcut("execution(* com.sprint.mission..*create*(..))")
//    public void create(){}

    @Pointcut("execution(* com.sprint.mission.repository..*del*(..))")
    public void delete(){}

    @Pointcut("execution(* com.sprint.mission.repository..save(..))")
    public void save(){}

    @Order(2)
    @AfterReturning("delete() || save()")
    public void setTimeLog(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();

        for (Object target : args) {
            switch (target){

                case User user -> {
                    user.setUpdateAt(Instant.now());
                    log.info("[AOP for UpdateAt] User {} updateAt: {}", user.getName(), user.getUpdateAt());
                }

                case Channel channel -> {
                    channel.setUpdatedAt(Instant.now());
                    log.info("[AOP for UpdateAt] Channel {} updateAt: {}", channel.getName(), channel.getUpdatedAt());
                }

                case Message message -> {
                    message.setUpdateAt(Instant.now());
                    log.info("[AOP for UpdateAt] Message {} updateAt: {}", message.getContent(), message.getUpdateAt());
                }

                default -> log.info("Fail to update UpdateAt Filed : 지원하지 않는 객체 타입 {}",target.getClass().getSimpleName());
            }
        }
    }
}
