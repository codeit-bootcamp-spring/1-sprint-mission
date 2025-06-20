package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.repository.EmitterRepository;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseConnectionCleaner {

  private final EmitterRepository emitterRepository;

  /**
   * Ping 했을 때 Pong 없으면 삭제
   **/
  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void cleanUpConnection() {
    log.info("SSE 연결 상태 확인 시작");
    // 1. Emitter의 모든 객체를 받아온다.
    Map<String, SseEmitter> emitters = emitterRepository.findAllEmitter();

    // 2. try-catch -> 더미 이벤트 전송 ->  예외 발생시 삭제한다.

    emitters.forEach((emitterId, emitter) -> {
          try {

            emitter.send(SseEmitter.event()
                .id(emitterId + "_ping")
                .name("ping")
                .data("ping"));
            log.trace("ping 전송 완료: emitterId={}", emitterId);

          } catch (IOException e) {
            log.info("Pong 반응 없음 : emitterId={} ", emitterId);
            emitterRepository.deleteById(emitterId);
          }
        }
    );

  }

}
