package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.quota.ClientQuotaAlteration.Op;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private final NotificationMapper notificationMapper;

    @KafkaListener(topics = {
            "discodeit.new-message",
            "discodeit.role-change",
            "discodeit.async-failed"
    }, groupId = "notification-group")
    public void consume(String payload) {
        try {
            NotificationEvent event = objectMapper.readValue(payload, NotificationEvent.class);
            log.info("Kafka 알림 수신: {}", event);

            for (UUID receiverId : event.getReceivers()) {
                List<NotificationDto> updated = notificationRepository.findAllByReceiver_Id(
                                receiverId)
                        .stream().map(notificationMapper::toDto).toList();
                cacheManager.getCache("userNotifications").put(receiverId, updated);
                userRepository.findById(receiverId).ifPresent(receiver -> {
                    Notification notification = Notification.of(
                            receiver,
                            event.getTitle(),
                            event.getContent(),
                            event.getType(),
                            event.getTargetId()
                    );
                    notificationRepository.save(notification);
                    log.info("알림 저장 완료 : receiver+{}, type={}", receiver, event.getType());
                });
            }
        } catch (Exception e) {
            log.error("알림 처리 실패", e);
        }
    }
}
