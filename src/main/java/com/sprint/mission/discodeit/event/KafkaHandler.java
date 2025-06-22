package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Async("eventTaskExecutor")
  @EventListener
  public void handleNewMessageEvent(NewMessageEvent event) throws JsonProcessingException {
    log.info("새 메세지 생성 이벤트 kafka 전송: {}", event);
    String payload = objectMapper.writeValueAsString(event);
    kafkaTemplate.send("discodeit.new_message", event.getContent(), payload);
  }

  @Async("eventTaskExecutor")
  @EventListener
  public void handleAsyncTaskFailedEvent(AsyncTaskFailedEvent event)
      throws JsonProcessingException {
    log.info("비동기 파일 업도드 실패 이벤트 kafka 전송: {}", event);
    String payload = objectMapper.writeValueAsString(event);
    kafkaTemplate.send("discodeit.async_task_failed", event.getContent(), payload);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleRoleChangedEvent(UserRoleChangedEvent event) throws JsonProcessingException {
    log.info("사용자 권한 변경 이벤트 kafka 전송: {}", event);
    String payload = objectMapper.writeValueAsString(event);
    kafkaTemplate.send("discodeit.role_changed", event.getContent(), payload);
  }
}
