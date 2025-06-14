package com.sprint.mission.discodeit.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.CacheConfig;
import com.sprint.mission.discodeit.dto.kafka.KafkaAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaNewMessageEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaNotificationEvent;
import com.sprint.mission.discodeit.dto.kafka.KafkaRoleChangedEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kafka 기반 알림 처리 기존 Spring Event 기반 NotificationEventListener를 Kafka Consumer로 전환
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaNotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    /**
     * 새 미시지 이벤트
     */
    @KafkaListener(topics = "{$discodeit.kafka.topics.new-message}")
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @Transactional
    public void handleNewMessageEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        ConsumerRecord<String, String> record,
        Acknowledgment acknowledgment
    ) {
        try {
            log.debug("새 메시지 Kafka 이벤트 처리 시작: topic={}, partition={}, offset={}", topic, partition,
                offset);

            KafkaNewMessageEvent event = objectMapper.readValue(message,
                KafkaNewMessageEvent.class);
            log.debug("새 메시지 이벤트 파싱 완료: channelId={}", event.channelId());

            List<ReadStatus> notificationEnabledStatuses = readStatusRepository
                .findAllByChannelIdWithUserWhereNotificationEnabled(event.channelId());

            List<UUID> notificationTargetIds = notificationEnabledStatuses.stream()
                .filter(readStatus -> !readStatus.getUser().getId().equals(event.authorId()))
                .map(readStatus -> {
                    User receiver = readStatus.getUser();
                    String title = "새로운 메시지";
                    String content = "새로운 메시지가 도착했습니다: " +
                        (event.content().length() > 50 ?
                            event.content().substring(0, 50) + "..." :
                            event.content());

                    Notification notification = new Notification(
                        receiver, title, content, NotificationType.NEW_MESSAGE, event.channelId()
                    );
                    notificationRepository.save(notification);
                    log.debug("새 메시지 알림 생성: receiverId={}, channelId={}",
                        receiver.getId(), event.channelId());

                    return receiver.getId();
                })
                .toList();

            notificationTargetIds.forEach(this::evictUserNotificationsCache);

            log.info("새 메시지 Kafka 이벤트 처리 완료: channelId={}, 알림 대상자 {}명",
                event.channelId(), notificationTargetIds.size());

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("새 메시지 Kafka 이벤트 처리 실패: topic={}, partition={}, offset={}", topic, partition,
                offset, e);
            throw new RuntimeException("새 메시지 이벤트 처리 실패", e);
        }
    }

    /**
     * 권한 변경 이벤트
     */
    @KafkaListener(topics = "${discodeit.kafka.topics.role-changed}")
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @Transactional
    public void handleRoleChangedEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        ConsumerRecord<String, String> record,
        Acknowledgment acknowledgment
    ) {
        try {
            log.debug("권한 변경 Kafka 이벤트 처리 시작: topic={}, partition={}, offset={}", topic, partition,
                offset);

            KafkaRoleChangedEvent event = objectMapper.readValue(message,
                KafkaRoleChangedEvent.class);
            log.debug("권한 변경 이벤트 파싱 완료: userId={}, oldRole={}, newRole={}",
                event.userId(), event.oldRole(), event.newRole());

            User user = userRepository.findById(event.userId()).orElse(null);
            if (user != null) {
                String title = "권한이 변경되었습니다";
                String content = String.format("권한이 %s에서 %s로 변경되었습니다.",
                    event.oldRole().name(), event.newRole().name());

                Notification notification = new Notification(
                    user, title, content, NotificationType.ROLE_CHANGED, event.userId()
                );
                notificationRepository.save(notification);

                evictUserNotificationsCache(event.userId());
                log.info("권한 변경 Kafka 알림 생성: userId={}, oldRole={}, newRole={}",
                    event.userId(), event.oldRole(), event.newRole());
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("권한 변경 Kafka 이벤트 처리 실패: topic={}, partition={}, offset={}", topic, partition,
                offset, e);
            throw new RuntimeException("권한 변경 이벤트 처리 실패", e);
        }
    }

    /**
     * 비동기 작업 실패 이벤트
     */
    @KafkaListener(topics = "${discodeit.kafka.topics.async-task-failed}")
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @Transactional
    public void handleAsyncTaskFailedEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        ConsumerRecord<String, String> record,
        Acknowledgment acknowledgment
    ) {
        try {
            log.debug("비동기 작업 실패 Kafka 이벤트 처리 시작: topic={}, partition={}, offset={}", topic,
                partition, offset);

            KafkaAsyncTaskFailedEvent event = objectMapper.readValue(message,
                KafkaAsyncTaskFailedEvent.class);
            log.debug("비동기 작업 실패 이벤트 파싱 완료: userId={}, taskName={}",
                event.userId(), event.taskName());

            User user = userRepository.findById(event.userId()).orElse(null);
            if (user != null) {
                String title = "작업 처리 실패";
                String content = String.format("요청하신 작업(%s)이 실패했습니다. 다시 시도해 주세요.",
                    event.taskName());

                Notification notification = new Notification(
                    user, title, content, NotificationType.ASYNC_FAILED, null
                );
                notificationRepository.save(notification);

                evictUserNotificationsCache(event.userId());
                log.info("비동기 작업 실패 Kafka 알림 생성: userId={}, taskName={}",
                    event.userId(), event.taskName());
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("비동기 작업 실패 Kafka 이벤트 처리 실패: topic={}, partition={}, offset={}", topic,
                partition, offset, e);
            throw new RuntimeException("비동기 작업 실패 이벤트 처리 실패", e);
        }
    }

    /**
     * 일반 알림 이벤트
     */
    @KafkaListener(topics = "${discodeit.kafka.topics.notification}")
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @Transactional
    public void handleNotificationEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        ConsumerRecord<String, String> record,
        Acknowledgment acknowledgment
    ) {
        try {
            log.debug("일반 알림 Kafka 이벤트 처리 시작: topic={}, partition={}, offset={}", topic, partition,
                offset);

            KafkaNotificationEvent event = objectMapper.readValue(message,
                KafkaNotificationEvent.class);
            log.debug("일반 알림 이벤트 파싱 완료: receiverId={}, type={}",
                event.receiverId(), event.type());

            User receiver = userRepository.findById(event.receiverId()).orElse(null);
            if (receiver != null) {
                Notification notification = new Notification(
                    receiver, event.title(), event.content(), event.type(), event.targetId()
                );
                notificationRepository.save(notification);

                evictUserNotificationsCache(event.receiverId());
                log.info("일반 Kafka 알림 생성: receiverId={}, type={}",
                    event.receiverId(), event.type());
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("일반 알림 Kafka 이벤트 처리 실패: topic={}, partition={}, offset={}", topic, partition,
                offset, e);
            throw new RuntimeException("일반 알림 이벤트 처리 실패", e);
        }
    }

    private void evictUserNotificationsCache(UUID receiverId) {
        try {
            Cache cache = cacheManager.getCache(CacheConfig.USER_NOTIFICATIONS);
            if (cache != null) {
                cache.evict(receiverId);
                log.debug("사용자 알림 캐시 무효화 완료: receiverId={}", receiverId);
            } else {
                log.warn("알림 캐시를 찾을 수 없음: cacheName={}", CacheConfig.USER_NOTIFICATIONS);
            }
        } catch (Exception e) {
            log.error("알림 캐시 무효화 실패: receiverId={}", receiverId, e);
        }
    }
}
