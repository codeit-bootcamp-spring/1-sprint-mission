package com.sprint.mission.discodeit.common.aop.logging;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

  @Around("execution(* com.sprint.mission.discodeit.service.UserService.createUser(..)) || " +
      "execution(* com.sprint.mission.discodeit.service.UserService.updateUser(..)) || " +
      "execution(* com.sprint.mission.discodeit.service.UserService.deleteUser(..))")
  public Object logUserControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    return getObject(joinPoint);
  }

  @Around(
      "execution(* com.sprint.mission.discodeit.service.ChannelService.createPublicChannel(..)) || "
          +
          "execution(* com.sprint.mission.discodeit.service.ChannelService.createPrivateChannel(..)) || "
          +
          "execution(* com.sprint.mission.discodeit.service.ChannelService.updateChannel(..)) || "
          +
          "execution(* com.sprint.mission.discodeit.service.ChannelService.deleteChannel(..))")
  public Object logChannelControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    return getObject(joinPoint);
  }

  @Around(
      "execution(* com.sprint.mission.discodeit.service.MessageService.createMessage(..)) || "
          +
          "execution(* com.sprint.mission.discodeit.service.MessageService.updateMessage(..)) || "
          +
          "execution(* com.sprint.mission.discodeit.service.MessageService.deleteMessage(..))")
  public Object logMessageControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    return getObject(joinPoint);
  }

  @Around(
      "execution(* com.sprint.mission.discodeit.service.BinaryContentService.findBinaryContentById(..))")
  public Object logBinaryContentControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    return getObject(joinPoint);
  }

  private Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs(); // 메서드 인자 가져오기

    log.info("Service Method {} - START, Args: {}", methodName, Arrays.toString(args));

    Object result = joinPoint.proceed(); // 실제 메서드 실행

    log.info("Service Method {} - END, Return: {}", methodName, result);
    return result;
  }
}

