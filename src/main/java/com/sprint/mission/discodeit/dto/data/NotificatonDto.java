package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificatonType;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record NotificatonDto (
    UUID id, Instant createAt, User receviedId,
    String title, NotificatonType type, Optional<UUID> targetId
){}