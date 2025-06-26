package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

  private final SseEmitterRepository emitterRepository;
  private final UserRepository userRepository;

  public SseEmitter subscribe(String username, String lastEventId) {
    log.debug("SSE 구독 시작: username={}, lastEventId={}", username, lastEventId);

    SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유지
    String emitterId = UUID.randomUUID().toString();

    emitterRepository.save(username, emitterId, emitter);
    log.info("SSE emitter 저장됨: username={}, emitterId={}", username, emitterId);

    emitter.onCompletion(() -> emitterRepository.remove(username, emitterId));
    emitter.onTimeout(() -> emitterRepository.remove(username, emitterId));
    emitter.onError(e -> emitterRepository.remove(username, emitterId));

    try {
      emitter.send(SseEmitter.event()
          .id(emitterId)
          .name("connect")
          .data("SSE connected"));
    } catch (IOException e) {
      throw new RuntimeException("SSE 초기 연결 실패", e);
    }

    return emitter;
  }

  public void send(String username, NotificationDto notification) {
    List<SseEmitter> emitters = emitterRepository.findAllEmittersByUsername(username);
    log.debug("SSE 전송 시도: username={}, emitter 수={}", username, emitters.size());

    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(SseEmitter.event()
            .id(notification.id().toString())
            .name("notifications")
            .data(notification));
        log.info("SSE 전송 성공: username={}, notificationId={}", username, notification.id());
      } catch (IOException e) {
        log.warn("SSE 전송 실패: username={}, error={}", username, e.getMessage());
        emitter.completeWithError(e);
      }
    }
  }

  public void sendChannelsRefreshEventToAll(UUID channelId) {
    Map<String, List<SseEmitter>> allEmitters = emitterRepository.findAllEmitters(); // 모든 사용자 emitter 가져오기

    for (Map.Entry<String, List<SseEmitter>> entry : allEmitters.entrySet()) {
      String username = entry.getKey();
      for (SseEmitter emitter : entry.getValue()) {
        try {
          emitter.send(SseEmitter.event()
              .id(UUID.randomUUID().toString())
              .name("channels.refresh")
              .data(Map.of("channelId", channelId)));
          log.info("SSE 채널 목록 갱신 이벤트 전송: username={}, channelId={}", username, channelId);
        } catch (IOException e) {
          log.warn("SSE 채널 목록 갱신 이벤트 실패: username={}, error={}", username, e.getMessage());
          emitter.completeWithError(e);
        }
      }
    }
  }

  public void sendUsersRefreshEvent(UUID userId) {
    Map<String, List<SseEmitter>> allEmitters = emitterRepository.findAllEmitters();

    for (Map.Entry<String, List<SseEmitter>> entry : allEmitters.entrySet()) {
      String username = entry.getKey();
      List<SseEmitter> emitters = entry.getValue();

      for (SseEmitter emitter : emitters) {
        try {
          emitter.send(SseEmitter.event()
              .id(UUID.randomUUID().toString())
              .name("users.refresh")
              .data(Map.of("userId", userId))
          );
          log.info("사용자 목록 갱신 이벤트 전송: username={}, userId={}", username, userId);
        } catch (IOException | IllegalStateException e) {
          emitter.completeWithError(e);
        }
      }
    }
  }

  public void sendBinaryContentStatusUpdate(String username, BinaryContentDto contentDto) {
    List<SseEmitter> emitters = emitterRepository.findAllEmittersByUsername(username);
    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(SseEmitter.event()
            .id(contentDto.id().toString())
            .name("binaryContents.status")
            .data(contentDto));
        log.info("파일 상태 변경 SSE 전송: username={}, contentId={}, status={}",
            username, contentDto.id(), contentDto.uploadStatus());
      } catch (IOException | IllegalStateException e) {
        log.warn("파일 상태 변경 SSE 실패: username={}, error={}", username, e.getMessage());
        emitter.completeWithError(e);
      }
    }
  }

}

