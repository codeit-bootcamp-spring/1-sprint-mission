package com.sprint.mission.aop.usedAOP;

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

//    @Pointcut("execution(* com.sprint.mission.repository..*del*(..))")
//    public void delete(){} 삭제는 애초에 업데이트가 필요 없네

    @Pointcut("execution(* com.sprint.mission.repository.jcf.main..save(..))")
    public void jcfMainSave(){}

    @Pointcut("execution(* com.sprint.mission.repository.file.main..save(..))")
    public void fileMainSave(){}

    @Order(2)
    @AfterReturning("jcfMainSave()") // 지금은 jpa를 안 쓰니 update후 무조건 save를 해야 하므로 save만 있어도 가능
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

                default -> {}
//                default -> log.info("Fail to update UpdateAt Filed : 지원하지 않는 객체 타입 {}",target.getClass().getSimpleName());
            }
        }
    }
}
