package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.sse.SseEventSender;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private SseEventSender sseEventSender;
    @Autowired
    private NotificationMapper notificationMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void handleNotification(NotificationEvent event) {
        /*log.info("📩 NotificationEventListener 호출됨: {}", event.getReceiver());
        User receiver = userRepository.findById(event.getReceiver())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        log.info("Notification event received: type={},receiverId={}", event.getType(),
                event.getReceiver());
        Notification notification = Notification.of(
                receiver,
                event.getTitle(),
                event.getContent(),
                event.getType(),
                event.getTargetId()
        );
        notificationRepository.save(notification);
        log.debug("알림 저장 완료: receiver={}, type={}, title={}",
                receiver.getId(), event.getType(), event.getTitle());*/

        for (UUID receiverId : event.getReceivers()) {
            Optional.ofNullable(cacheManager.getCache("userNotifications"))
                    .ifPresent(cache -> cache.evictIfPresent(receiverId));
            userRepository.findById(receiverId).ifPresent(receiver -> {
                Notification notification =
                        Notification.of(
                                receiver,
                                event.getTitle(),
                                event.getContent(),
                                event.getType(),
                                event.getTargetId()
                        );
                notificationRepository.save(notification);
                log.debug("알림 저장 완료: receiver={}, type={}, title={}",
                        receiver.getId(), event.getType(), event.getTitle());
                sseEventSender.sendNotification(receiver.getId(),
                        notificationMapper.toDto(notification));
            });
        }
    }

    @Recover
    public void recoverNotification(Exception e, NotificationEvent event) {
        log.error("[Recover] 비동기 처리 실패 알림 - 수신자: {}", event.getReceivers(), e);

        for (UUID receiverId : event.getReceivers()) {
            userRepository.findById(receiverId).ifPresent(receiver -> {
                Notification notification = Notification.of(
                        receiver,
                        "알림 발송 실패",
                        "알림 처리 도중 문제 발생",
                        NotificationType.ASYNC_FAILED,
                        null
                );
                notificationRepository.save(notification);
                log.warn("실패 알림 저장 완료 - 사용자 ID: {}", receiver.getId());
            });
        }
    }
}
