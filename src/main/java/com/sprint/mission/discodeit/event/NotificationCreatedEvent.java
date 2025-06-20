package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationCreatedEvent {

    private final UUID userId;
    private final NotificationResponse notificationResponse;
}
