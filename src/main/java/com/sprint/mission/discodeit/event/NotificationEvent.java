package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class NotificationEvent {

    private final List<UUID> receivers;
    private final String title;
    private final String content;
    private final NotificationType type;
    private final UUID targetId;
}
