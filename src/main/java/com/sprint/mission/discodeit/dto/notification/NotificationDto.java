package com.sprint.mission.discodeit.dto.notification;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {

    private UUID id;
    private Instant createdAt;
    private UUID receiverId;
    private String title;
    private String content;
    private NotificationType type;
    private UUID targetId;
}
