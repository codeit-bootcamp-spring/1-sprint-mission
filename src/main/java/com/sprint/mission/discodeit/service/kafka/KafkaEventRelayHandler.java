package com.sprint.mission.discodeit.service.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.dto.event.NewMessageEvent;
import com.sprint.mission.discodeit.dto.event.NotificationEvent;
import com.sprint.mission.discodeit.dto.event.RoleChangedEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaNewMessageEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaNotificationEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaRoleChangedEvent;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


/**
 * Spring Event Kafka 메시지로 중계
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaEventRelayHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${discodeit.kafka.topics.new-message}")
    private String newMessageTopic;

    @Value("${discodeit.kafka.topics.role-changed}")
    private String roleChangedTopic;

    @Value("${discodeit.kafka.topics.async-task-failed}")
    private String asyncTaskFailedTopic;

    @Value("${discodeit.kafka.topics.notification}")
    private String notificationTopic;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void relayNewMessageEvent(NewMessageEvent event) {
        try {
            log.debug("새 메시지 이벤트 Kafka 중계 시작: channelId={}", event.channelId());

            KafkaNewMessageEvent kafkaEvent = KafkaNewMessageEvent.from(event, newMessageTopic);
            String message = objectMapper.writeValueAsString(kafkaEvent);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
                newMessageTopic,
                event.channelId().toString(), message);

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    log.info("새 메시지 이벤트 Kafka 전송 성공: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("새 메시지 이벤트 Kafka 전송 실패: channelId={}", event.channelId(), throwable);
                    throw new RuntimeException("Kafka 메시지 전송 실패", throwable);
                }
            });
        } catch (Exception e) {
            log.error("새 메시지 이벤트 Kafka 중계 실패: channelId={}", event.channelId(), e);
            throw new RuntimeException("Kafka 이벤트 중계 실패", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void relayRoleChangedEvent(RoleChangedEvent event) {
        try {
            log.debug("권한 변경 이벤트 Kafka 중계 시작: userId={}", event.userId());

            KafkaRoleChangedEvent kafkaEvent = KafkaRoleChangedEvent.from(event, roleChangedTopic);
            String message = objectMapper.writeValueAsString(kafkaEvent);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
                roleChangedTopic, event.userId().toString(), message);

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    log.info("권한 변경 이벤트 Kafka 전송 성공: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("권한 변경 이벤트 Kafka 전송 실패: userId={}", event.userId(), throwable);
                    throw new RuntimeException("Kafka 메시지 전송 실패", throwable);
                }
            });
        } catch (Exception e) {
            log.error("권한 변경 이벤트 Kafka 중계 실패: userId={}", event.userId(), e);
            throw new RuntimeException("Kafka 이벤트 중계 실패", e);

        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void relayAsyncFailedEvent(AsyncTaskFailedEvent event) {
        try {
            log.debug("비동기 작업 실패 이벤트 Kafka 중계 시작: userId={}", event.userId());

            KafkaAsyncTaskFailedEvent kafkaEvent = KafkaAsyncTaskFailedEvent.from(event,
                asyncTaskFailedTopic);
            String message = objectMapper.writeValueAsString(kafkaEvent);

            CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(asyncTaskFailedTopic, event.userId().toString(), message);

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    log.info("비동기 작업 실패 이벤트 Kafka 전송 성공: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("비동기 작업 실패 이벤트 Kafka 전송 실패: userId={}", event.userId(), throwable);
                    throw new RuntimeException("Kafka 메시지 전송 실패", throwable);
                }
            });
        } catch (Exception e) {
            log.error("비동기 작업 실패 이벤트 Kafka 중계 실패: userId={}", event.userId(), e);
            throw new RuntimeException("Kafka 이벤트 중계 실패", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void relayNotificationEvent(NotificationEvent event) {
        try {
            log.debug("알림 이벤트 Kafka 중계 시작: receiverId={}", event.receiverId());

            KafkaNotificationEvent kafkaEvent = KafkaNotificationEvent.from(event,
                notificationTopic);
            String message = objectMapper.writeValueAsString(kafkaEvent);

            CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(notificationTopic, event.receiverId().toString(), message);

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    log.info("알림 이벤트 Kafka 전송 성공: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("알림 이벤트 Kafka 전송 실패: receiverId={}", event.receiverId(), throwable);
                    throw new RuntimeException("Kafka 메시지 전송 실패", throwable);
                }
            });
        } catch (Exception e) {
            log.error("알림 이벤트 Kafka 중계 실패: receiverId={}", event.receiverId(), e);
            throw new RuntimeException("Kafka 이벤트 중계 실패", e);
        }
    }
}
