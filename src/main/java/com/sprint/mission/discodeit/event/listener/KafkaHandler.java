package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.AuthenticatedAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NewMessageEvent event) {
    log.debug("새 메시지 Kafka 전송 시작: messageId={}", event.messageDto().id());
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("discodeit.new_message", event.messageDto().id().toString(), payload);
      log.info("새 메시지 Kafka 전송 완료: messageId={}", event.messageDto().id());
    } catch (Exception e) {
      log.error("새 메시지 Kafka 전송 실패: messageId={}, error={}",
          event.messageDto().id(), e.getMessage(), e);
    }
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(RoleChangedEvent event) {
    log.debug("권한 변경 Kafka 전송 시작: userId={}", event.userId());
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("discodeit.role_changed", event.userId().toString(), payload);
      log.info("권한 변경 Kafka 전송 완료: userId={}", event.userId());
    } catch (Exception e) {
      log.error("권한 변경 Kafka 전송 실패: userId={}, error={}",
          event.userId(), e.getMessage(), e);
    }
  }

  @Async("eventTaskExecutor")
  @EventListener
  public void handle(AsyncTaskFailedEvent event) {
    log.debug("비동기 작업 실패 Kafka 전송 시작: requestId={}",
        event.asyncTaskFailure().getRequestId());
    try {
      SecurityContext context = SecurityContextHolder.getContext();
      UUID authenticatedUserId =
          context.getAuthentication().isAuthenticated() && context.getAuthentication()
              .getPrincipal() instanceof DiscodeitUserDetails userDetails
              ? userDetails.getUserDto().id()
              : null;

      AuthenticatedAsyncTaskFailedEvent authenticatedAsyncTaskFailedEvent = new AuthenticatedAsyncTaskFailedEvent(
          event, authenticatedUserId);
      String payload = objectMapper.writeValueAsString(authenticatedAsyncTaskFailedEvent);
      kafkaTemplate.send("discodeit.async_task_failed",
          event.asyncTaskFailure().getRequestId().toString(), payload);
      log.info("비동기 작업 실패 Kafka 전송 완료: requestId={}",
          event.asyncTaskFailure().getRequestId());
    } catch (Exception e) {
      log.error("비동기 작업 실패 Kafka 전송 실패: requestId={}, error={}",
          event.asyncTaskFailure().getRequestId(), e.getMessage(), e);
    }
  }
} 