package com.sprint.mission.discodeit.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.AuthenticatedAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Async("eventExecutor")
  @TransactionalEventListener
  public void handle(NewMessageEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("discodeit.new_message", event.messageDto().id().toString(), payload);
    } catch (Exception e) {
      log.error("새 메시지 Kafka 전송 실패: messageId={}, error={}",
          event.messageDto().id(), e.getMessage(), e);
    }
  }

  @Async("eventExecutor")
  @TransactionalEventListener
  public void handle(RoleChangedEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("discodeit.role_changed", event.userId().toString(), payload);
    } catch (Exception e) {
      log.error("권한 변경 Kafka 전송 실패: userId={}, error={}",
          event.userId(), e.getMessage(), e);
    }
  }

  @Async("eventExecutor")
  @EventListener
  public void handle(AsyncTaskFailedEvent event) {
    try {
      SecurityContext context = SecurityContextHolder.getContext();
      UUID authenticatedUserId =
          context.getAuthentication().isAuthenticated() && context.getAuthentication()
              .getPrincipal() instanceof DiscodeitUserDetails userDetails
              ? userDetails.getUser().id()
              : null;

      AuthenticatedAsyncTaskFailedEvent authenticatedAsyncTaskFailedEvent =
          new AuthenticatedAsyncTaskFailedEvent(event, authenticatedUserId);
      String payload = objectMapper.writeValueAsString(authenticatedAsyncTaskFailedEvent);
      kafkaTemplate.send("discodeit.async_task_failed",
          event.asyncTaskFailure().requestId(), payload);
    } catch (Exception e) {
      log.error("비동기 작업 실패 Kafka 전송 실패: requestId={}, error={}",
          event.asyncTaskFailure().requestId(), e.getMessage(), e);
    }
  }

}
