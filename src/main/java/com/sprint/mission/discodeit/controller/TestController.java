package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.async.event.FileUploadEvent;
import com.sprint.mission.discodeit.common.CacheStatsLogger;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

  // todo: 프로메테우스 테스트를 위한 컨트롤러!
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher applicationEventPublisher;

  private final CacheStatsLogger cacheStatsLogger;

  @Timed(value = "upload.sync", description = "동기 업로드 처리 시간")
  @PostMapping("/sync")
  public ResponseEntity<String> syncUpload(@RequestParam MultipartFile file)
      throws InterruptedException {
    Thread.sleep(500); // 의도적 지연
    binaryContentStorage.put(UUID.randomUUID(), file).join(); // 동기 처리
    return ResponseEntity.ok("sync complete");
  }

  @Timed(value = "upload.async", description = "비동기 업로드 처리 시간")
  @PostMapping("/async")
  public ResponseEntity<String> asyncUpload(@RequestParam MultipartFile file,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UUID fileId = UUID.randomUUID();
    applicationEventPublisher.publishEvent(
        new FileUploadEvent(fileId, file, userDetails.getUser().getId())); // 비동기 처리
    return ResponseEntity.ok("async complete");
  }

  @GetMapping("/cache-stats")
  public ResponseEntity<Void> printCacheStats() {
    cacheStatsLogger.logStats("userChannels");
    cacheStatsLogger.logStats("userNotifications");
    return ResponseEntity.ok().build();
  }

}
