package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class NotificationEvent {

    private final UUID receiverId;
    private final NotificationType type;
    private final UUID targetId;
    private final String title;
    private final String content;
}
