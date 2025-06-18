//package com.sprint.mission.discodeit.Async;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ExecutorTester implements ApplicationRunner {
//
//  private final ThreadPoolTaskExecutor fileUploadExecutor;
//
//  public ExecutorTester(
//      @Qualifier("fileUploadExecutor") ThreadPoolTaskExecutor fileUploadExecutor) {
//    this.fileUploadExecutor = fileUploadExecutor;
//  }
//
//  @Override
//  public void run(ApplicationArguments args) {
//    // Application이 완전히 기동된 직후 한 번만 실행됨
//    for (int i = 0; i < 5; i++) {
//      int index = i;
//      fileUploadExecutor.execute(() -> {
//        try {
//          Thread.sleep(5000); // 5초 동안 블로킹
//          System.out.println("테스트 태스크 완료: " + index);
//        } catch (InterruptedException e) {
//          Thread.currentThread().interrupt();
//        }
//      });
//    }
//    System.out.println("테스트용 5개 태스크 제출 완료");
//  }
//}
