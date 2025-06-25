package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.event.FileUploadStatusChangedEvent;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.event.PrivateChannelListChangedEvent;
import com.sprint.mission.discodeit.event.PublicChannelListChangedEvent;
import com.sprint.mission.discodeit.event.UserListChangedEvent;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * SSE 이벤트 전송 - 단일 사용자만 동기 처리하고 여러 사용자는 비동기 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventSender {

    private static final String EVENT_NOTIFICATIONS = "notifications";
    private static final String EVENT_BINARY_CONTENT_STATUS = "binaryContents.status";
    private static final String EVENT_CHANNELS_REFRESH = "channels.refresh";
    private static final String EVENT_USERS_REFRESH = "users.refresh";
    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void safelySendNotificationCreated(NotificationCreatedEvent event) {
        sseService.sendToUser(EVENT_NOTIFICATIONS, event.getNotificationResponse(),
            event.getUserId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void safelySendFileUploadStatusUpdated(FileUploadStatusChangedEvent event) {
        sseService.sendToUser(EVENT_BINARY_CONTENT_STATUS, event.getBinaryContentResponse(),
            event.getUserId());
    }

    @Async("eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void safelySendPrivateChannelRefresh(PrivateChannelListChangedEvent event) {
        for (UUID userId : event.getUserIds()) {
            sseService.sendToUser(EVENT_CHANNELS_REFRESH,
                Map.of("channelId", event.getChannelId()), userId);
        }
    }

    @Async("eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void safelySendPublicChannelRefresh(PublicChannelListChangedEvent event) {
        sseService.sendToAll(EVENT_CHANNELS_REFRESH, Map.of("channelId", event.getChannelId()));
    }

    @Async("eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void safelySendUserListRefresh(UserListChangedEvent event) {
        log.info("safelySendUserListRefresh (userId: {})", event.getUserId());
        sseService.sendToAll(EVENT_USERS_REFRESH, Map.of("userId", event.getUserId()));
    }

    @Async("eventExecutor")
    @EventListener
    public void safelySendUserListRefreshNow(UserListChangedEvent event) {
        log.info("safelySendUserListRefreshNow (userId: {})", event.getUserId());
        sseService.sendToAll(EVENT_USERS_REFRESH, Map.of("userId", event.getUserId()));
    }
}
