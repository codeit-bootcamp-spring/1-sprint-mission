package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.UserRoleChangedEvent;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

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

            notificationService.createNotification(event);
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

        notificationService.createNotification(event);
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

        notificationService.createNotification(event);
    }

    private String generateMessageNotificationTitle(ChannelType type, String channelName,
        String authorName) {
        return switch (type) {
            case PUBLIC -> "채널 " + channelName + "에 새로운 메시지가 있습니다.";
            case PRIVATE -> authorName + "님으로부터 새로운 메시지가 있습니다.";
        };
    }
}
