package com.sprint.mission.aop.notUsedAOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
//
////@Component
//@Slf4j
////@Aspect
//public class FileExceptionAOP {
//
//    @Pointcut("execution(* com.sprint.mission.repository.file.*(..))")
//    public void repositoryFile(){}
//
//    @Pointcut("execution(* com.sprint.mission.service.file.*(..))")
//    public void serviceFile(){}
//
////    @Pointcut("execution(* com.sprint.mission.controller.file.*(..))")
////    public void controllerFile(){}
//
//
//    @Around("repositoryFile() || serviceFile()") //
//    public Object handleFileExceptions(ProceedingJoinPoint joinPoint) {
//        try {
//            return joinPoint.proceed(); // 원래 메서드 실행
//        } catch (NoSuchFileException e) {
//            //log.error("Cannot find File: {}", e.getMessage());
//            throw new FileProcessingException("Cannot find File.", e.getCause());
//        } catch (IOException e) {
//            throw new FileProcessingException("I/O Exception.", e.getCause());
//        } catch (ClassNotFoundException e) {
//            throw new FileProcessingException("Cannot find Class", e.getCause());
//        } catch (Throwable e) {
//            throw new RuntimeException("Occur Exception", e.getCause());
//        }
//    }
//
//
//    private static class FileProcessingException extends RuntimeException{
//        public FileProcessingException(String message, Throwable cause) {
//            super(message, cause);
//        }
//    }
//
//}
