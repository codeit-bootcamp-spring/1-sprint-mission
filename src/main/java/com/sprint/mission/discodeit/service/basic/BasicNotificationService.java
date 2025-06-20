package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.UserRoleChangedEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Cacheable(cacheNames = "notifications", key = "#receiverId", unless = "#result.isEmpty()")
    @Override
    public List<NotificationResponse> getMyNotifications(UUID receiverId) {
        return notificationRepository.getAllByReceiverId(receiverId).stream()
            .map(notificationMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "notifications", key = "#receiverId")
    @Transactional
    @Override
    public void readNotification(UUID id, UUID receiverId) {
        notificationRepository.deleteByIdAndReceiverId(id, receiverId);
    }

    @CacheEvict(cacheNames = "notifications", key = "#notificationEvent.receiverId")
    @Async("notificationExecutor")
    @EventListener
    @Transactional
    @Retryable(
        value = TransientDataAccessException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void createNotification(NotificationEvent notificationEvent) {
        UUID userId = notificationEvent.getReceiverId();

        log.info("이벤트 발행 확인: {} / {}", notificationEvent.getType(), userId);
        Notification notification = Notification.create(
            userId,
            notificationEvent.getTitle(),
            notificationEvent.getContent(),
            notificationEvent.getType(),
            notificationEvent.getTargetId());

        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = notificationMapper.entityToDto(savedNotification);

        eventPublisher.publishEvent(new NotificationCreatedEvent(userId, response));
    }

    @Recover
    public void recover(Exception e, NotificationEvent notificationEvent) {
        log.error("알림 전송 재시도 실패 : {}", notificationEvent.getReceiverId(), e);
    }

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyNewMessage(NewMessageEvent newMessageEvent) {

        List<UUID> receiverIds = newMessageEvent.getReceiverIds();
        UUID channelId = newMessageEvent.getChannelId();
        UUID authorId = newMessageEvent.getAuthorId();
        String content = newMessageEvent.getContent();

        String title = generateMessageNotificationTitle(
            newMessageEvent.getChannelType(),
            newMessageEvent.getChannelName(),
            newMessageEvent.getAuthorName()
        );

        for (UUID receiverId : receiverIds) {
            if (receiverId.equals(authorId)) {
                continue;
            }
            NotificationEvent event = NotificationEvent.builder()
                .receiverId(receiverId)
                .type(NotificationType.NEW_MESSAGE)
                .targetId(channelId)
                .title(title)
                .content(content)
                .build();
            eventPublisher.publishEvent(event);
        }
    }

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyUserRoleChanged(UserRoleChangedEvent userRoleChangedEvent) {
        UUID userId = userRoleChangedEvent.getUserId();
        String username = userRoleChangedEvent.getUsername();
        Role newRole = userRoleChangedEvent.getNewRole();
        Role oldRole = userRoleChangedEvent.getOldRole();

        String title = username + "님의 유저 권한이 변경되었습니다.";
        String content = oldRole.getDescription() + "에서 "
            + newRole.getDescription() + "(으)로 권한이 변경되었습니다.";

        NotificationEvent event = NotificationEvent.builder()
            .receiverId(userId)
            .type(NotificationType.ROLE_CHANGED)
            .targetId(userId)
            .title(title)
            .content(content)
            .build();
        eventPublisher.publishEvent(event);
    }

    // 비동기 실패 이벤트 리스너
    @Async("notificationExecutor")
    @EventListener
    public void notifyAsyncTaskFailure(AsyncTaskFailedEvent asyncTaskFailedEvent) {
        AsyncTaskFailure failure = asyncTaskFailedEvent.getFailure();

        SecurityContext context = SecurityContextHolder.getContext();
        UUID authenticatedUserId =
            context.getAuthentication().isAuthenticated() && context.getAuthentication()
                .getPrincipal() instanceof CustomUserDetails userDetails
                ? userDetails.getId()
                : null;

        String title = "비동기 작업 실패: " + failure.getTaskName();
        String content = "요청 ID: " + failure.getRequestId()
            + "\n실패 사유: " + failure.getFailureReason();

        NotificationEvent event = NotificationEvent.builder()
            .receiverId(authenticatedUserId)
            .type(NotificationType.ASYNC_FAILED)
            .targetId(failure.getRequestId())
            .title(title)
            .content(content)
            .build();
        eventPublisher.publishEvent(event);
    }

    private String generateMessageNotificationTitle(ChannelType type, String channelName,
        String authorName) {
        return switch (type) {
            case PUBLIC -> "채널 " + channelName + "에 새로운 메시지가 있습니다.";
            case PRIVATE -> authorName + "님으로부터 새로운 메시지가 있습니다.";
        };
    }
}
