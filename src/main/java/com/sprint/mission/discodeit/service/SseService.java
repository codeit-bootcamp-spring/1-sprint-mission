package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.repository.InMemoryEmitterRepository;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SseService {

  private static final long TIMEOUT = 30 * 60 * 1000L;

  private final InMemoryEmitterRepository emitterRepository;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public SseEmitter subscribe(UUID userId, String lastEventId) {

    String emitterId = makeEmitterId(userId);
    SseEmitter emitter = new SseEmitter(TIMEOUT);
    emitterRepository.save(userId, emitterId, emitter);

    Runnable cleanup = () -> emitterRepository.delete(userId, emitterId);
    emitter.onCompletion(cleanup);
    emitter.onTimeout(cleanup);
    emitter.onError(e -> cleanup.run());

    scheduler.scheduleAtFixedRate(
        () -> safeSend(emitter, emitterId, "ping", "keep-alive"),
        30, 30, TimeUnit.SECONDS);

    return emitter;
  }

  public <T> void push(UUID userId, String name, T data) {
    log.debug("SseService.push({}, {})", userId, name);
    emitterRepository.findEmitters(userId).forEach(emitter ->
        safeSend(emitter, UUID.randomUUID().toString(), name, data));
  }


  private String makeEmitterId(UUID userId) {
    return userId + "-" + System.currentTimeMillis();
  }

  private void safeSend(SseEmitter emitter, String id, String name, Object obj) {
    try {
      emitter.send(SseEmitter.event()
          .id(id)
          .name(name)
          .data(obj));
    } catch (Exception e) {
      log.warn("SSE 전송 실패 → emitter 제거: {}", e.toString());
      emitter.completeWithError(e);
    }
  }

}
