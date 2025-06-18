package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAspectJAutoProxy
@EnableAsync
@SpringBootTest(properties = {"jwt.secret=1cjasduqwncnzdasdqwcqwccqwcszc",
    "spring.aop.proxy-target-class=true"})
class LocalBinaryContentStorageTest {

  @Autowired
  private LocalBinaryContentStorage binaryContentStorage;

  @Autowired
  private MeterRegistry meterRegistry;

  @Test
  @DisplayName("비동기/동기 파일 업로드 처리 속도 비교")
  void upload() throws InterruptedException {
    UUID fileId = UUID.randomUUID();
    UUID fileId2 = UUID.randomUUID();
    UUID fileId3 = UUID.randomUUID();
    UUID fileId4 = UUID.randomUUID();
    byte[] dummyData = new byte[1024];
    byte[] dummyData2 = new byte[2048];
    byte[] dummyData3 = new byte[2048];
    byte[] dummyData4 = new byte[1024];

    binaryContentStorage.syncPut(fileId, dummyData);
    binaryContentStorage.syncPut(fileId2, dummyData2);
    binaryContentStorage.syncPut(fileId3, dummyData3);
    binaryContentStorage.syncPut(fileId4, dummyData4);
    Thread.sleep(50);

    CompletableFuture<UUID> future = binaryContentStorage.put(fileId, dummyData);
    CompletableFuture<UUID> future2 = binaryContentStorage.put(fileId2, dummyData2);
    CompletableFuture<UUID> future3 = binaryContentStorage.put(fileId3, dummyData3);
    CompletableFuture<UUID> future4 = binaryContentStorage.put(fileId4, dummyData4);
    future.join();
    future2.join();
    future3.join();
    future4.join();
    Thread.sleep(50);

    Timer syncTimer = meterRegistry.find("upload.sync").timer();
    Timer asyncTimer = meterRegistry.find("upload.async").timer();
    assertThat(syncTimer).isNotNull();
    assertThat(asyncTimer).isNotNull();

    double syncTotalMs = syncTimer.totalTime(TimeUnit.MILLISECONDS);
    double asyncTotalMs = asyncTimer.totalTime(TimeUnit.MILLISECONDS);
    System.out.println("asyncTotalMs = " + asyncTotalMs);
    System.out.println("syncTotalMs = " + syncTotalMs);
    ;
  }
}