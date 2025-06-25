package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicSseService implements SseService {

  private final EmitterRepository emitterRepository;


  @Override
  public SseEmitter subscribe(UUID userId, String lastEventId) {
    log.info("SSE 연결 시도 - userId: {}", userId);
    SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);

    emitter.onCompletion(() -> emitterRepository.remove(userId, emitter));
    emitter.onTimeout(() -> emitterRepository.remove(userId, emitter));
    emitter.onError(e -> emitterRepository.remove(userId, emitter));

    emitterRepository.save(userId, emitter);

    try {
      emitter.send(SseEmitter.event()
          .name("Connect")
          .data("SSE 연결에 성공했습니다."));
    } catch (IOException ioe) {
      // 메시지 전송 중 오류 발생 시 연결 종료
      emitter.completeWithError(ioe);
    }

    return emitter;
  }

  @Override
  public void sendNotification(UUID userId, NotificationDto notification) {
    log.info("📨 새 알림 생성, 알림 업데이트 필요");
    send(userId, "notifications", notification);
  }

  @Override
  public void sendBinaryContentStatus(UUID userId, BinaryContentResponseDto binaryContent) {
    log.info("📨 파일 업로드 상태 업데이트 필요");
    send(userId, "binaryContents.status", binaryContent);
  }

  @Override
  public void sendChannelRefresh(UUID userId, UUID channelId) {
    log.info("📨 채널 목록 업데이트 필요");
    send(userId, "channels.refresh", Map.of("channelId", channelId));

  }

  @Override
  public void sendChannelRefreshToIdList(List<UUID> userIds, UUID channelId) {
    for (UUID userId : userIds) {
      sendChannelRefresh(userId, channelId);
    }
  }

  @Override
  public void sendUserRefresh(UUID userId) {
    log.info("📨 사용자 목록 업데이트 필요");
    send(userId, "users.refresh", Map.of("userId", userId));

  }

  @Override
  public void send(UUID userId, String eventName, Object data) {
    for (SseEmitter emitter : emitterRepository.get(userId)) {
      try {
        emitter.send(SseEmitter.event()
            .id(UUID.randomUUID().toString())
            .name(eventName)
            .data(data));
      } catch (IOException ioe) {
        emitter.completeWithError(ioe);
        emitterRepository.remove(userId, emitter);
      }
    }
  }

  @Override
  @Scheduled(fixedRate = 30000)
  public void sendPing() {
    for (UUID userId : emitterRepository.getAllUserIds()) {
      for (SseEmitter emitter : emitterRepository.get(userId)) {
        try {

          emitter.send(SseEmitter.event()
              .id(UUID.randomUUID().toString())
              .name("ping")
              .data("keep-alive"));
        } catch (IOException ioe) {
          emitter.completeWithError(ioe);
        }
      }
    }
  }
}
