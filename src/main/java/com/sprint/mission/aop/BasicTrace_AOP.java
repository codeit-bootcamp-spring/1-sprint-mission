package com.sprint.mission.aop;

import com.sprint.mission.controller.ChannelController;
import com.sprint.mission.controller.MessageController;
import com.sprint.mission.controller.UserController;
import com.sprint.mission.controller.jcf.JCFChannelController;
import com.sprint.mission.controller.jcf.JCFMessageController;
import com.sprint.mission.controller.jcf.JCFUserController;
import com.sprint.mission.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Aspect
public class BasicTrace_AOP {

    // 이거 나중에 C-S-R 단계별 화살표 적용하기

    @Pointcut("execution(* com.sprint.mission.controller.*())")
    public void controllerMethod(){}

    @Pointcut("execution(* com.sprint.mission.service.*())")
    public void serviceMethod(){}

    @Pointcut("execution(* com.sprint.mission.controller.*())")
    public void repositoryMethod(){}

    @Around("controllerMethod() || serviceMethod() || repositoryMethod()")
    public Object doTrace(ProceedingJoinPoint joinPoint){

        String methodName = joinPoint.getSignature().getName();
        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        log.info("[{}] {} start", simpleName, methodName);
        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long resultTime = System.currentTimeMillis() - startTime;
            log.info("[{}] {} complete(time: {}ms", simpleName, methodName, resultTime);
            return result;
        } catch (Throwable e) {
            log.info("[{}] {} exception(cause: {}", simpleName, methodName, e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }



    static class Test {
        private static final ChannelController channelController = new JCFChannelController();
        private static final UserController userController = new JCFUserController();
        private static final MessageController messageController = new JCFMessageController();

        public static void main(String[] args) {
            List<User> userList = userCreateTest(3);

            // 수정
            User beforeUpdateUser = userList.get(0);
            System.out.println("BeforeUpdate => 닉네임: " + beforeUpdateUser.getName() + ", 비밀번호: " + beforeUpdateUser.getPassword());
            User afterUpdateUser = userController.updateUserNamePW(beforeUpdateUser.getId(), beforeUpdateUser.getName() + "-1", String.valueOf(1000 + new Random().nextInt(9000)));
            System.out.println("afterUpdate => 닉네임: " + afterUpdateUser.getName() + ", 비밀번호: " + afterUpdateUser.getPassword());

            // 나머지 시간 부
        }

        private static void findUserTest(){
            List<User> userList = userController.findAll().stream().toList();
            System.out.println("전체 유저 목록: " + userList + "유저 수: " + userList.size());
            User findUser = userController.findById(userList.get(0).getId());
            System.out.println("findById = " + findUser);
        }

        private static List<User> userCreateTest(int userCount) {
            for (int i = 1; i <= userCount; i++) {
                User creatUser = userController.create("유저" + i, String.valueOf(1000 + new Random().nextInt(9000)));
                System.out.println(creatUser.getName() + " 생성 완료");
            }
            System.out.println("전체 유저: " + userController.findAll());
            return userController.findAll().stream().toList();
        }
    }
}
