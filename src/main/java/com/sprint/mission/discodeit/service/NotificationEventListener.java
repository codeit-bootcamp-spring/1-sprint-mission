package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.dto.event.NewMessageEvent;
import com.sprint.mission.discodeit.dto.event.NotificationEvent;
import com.sprint.mission.discodeit.dto.event.RoleChangedEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BasicNotificationService notificationService;

    @Async
    @TransactionalEventListener
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void handleNotificationEvent(NotificationEvent event) {
        try {
            log.debug("알림 이벤트 처리 시작: receiverId={}, type={}", event.receiverId(), event.type());
            createNotification(event);

            notificationService.evictUserNotificationsCache(event.receiverId());

            log.info("알림 이벤트 처리 완료: receiverId={}, type={}", event.receiverId(), event.type());
        } catch (Exception e) {
            log.error("알림 이벤트 처리 실패: receiverId={}, type={}",
                event.receiverId(), event.type(), e);
            throw e;
        }
    }

    @Async
    @TransactionalEventListener
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void handleNewMessageEvent(NewMessageEvent event) {
        try {
            log.debug("새 메시지 이벤트 처리 시작: channelId={}", event.channelId());

            // 해당 채널에서 알림을 활성화한 사용자들 찾기 (작성자 제외)
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

            notificationTargetIds.forEach(receiverId -> {
                try {
                    notificationService.evictUserNotificationsCache(receiverId);
                    log.debug("새 메시지 알림 캐시 무효화: receiverId={}", receiverId);
                } catch (Exception e) {
                    log.warn("알림 캐시 무효화 실패: receiverId={}", receiverId, e);
                }
            });

            log.info("새 메시지 이벤트 처리 완료: channelId={}, 알림 대상자 {}명 - 알림 캐시 무효화",
                event.channelId(), notificationTargetIds.size());
        } catch (Exception e) {
            log.error("새 메시지 이벤트 처리 실패: channelId={}", event.channelId(), e);
            throw e;
        }
    }

    @Async
    @TransactionalEventListener
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void handleRoleChangedEvent(RoleChangedEvent event) {
        try {
            log.debug("권한 변경 이벤트 처리 시작: userId={}, oldRole={}, newRole={}",
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

                notificationService.evictUserNotificationsCache(event.userId());
                log.info("권한 변경 알림 생성: userId={}, oldRole={}, newRole={} - 알림 캐시 무효화",
                    event.userId(), event.oldRole(), event.newRole());
            }
        } catch (Exception e) {
            log.error("권한 변경 이벤트 처리 실패: userId={}", event.userId(), e);
            throw e;
        }
    }

    @Async
    @TransactionalEventListener
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void handleAsyncTaskFailedEvent(AsyncTaskFailedEvent event) {
        try {
            log.debug("비동기 작업 실패 이벤트 처리 시작: userId={}, taskName={}",
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

                notificationService.evictUserNotificationsCache(event.userId());

                log.info("비동기 작업 실패 알림 생성: userId={}, taskName={} - 알림 캐시 무효화",
                    event.userId(), event.taskName());
            }
        } catch (Exception e) {
            log.error("비동기 작업 실패 이벤트 처리 실패: userId={}", event.userId(), e);
            throw e;
        }
    }

    @Transactional
    protected void createNotification(NotificationEvent event) {
        User receiver = userRepository.findById(event.receiverId()).orElse(null);
        if (receiver != null) {
            Notification notification = new Notification(
                receiver, event.title(), event.content(), event.type(), event.targetId()
            );
            notificationRepository.save(notification);
        }
    }
}