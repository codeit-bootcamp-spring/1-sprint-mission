package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEntity {
    protected UUID id;
    protected Instant createdAt;
    protected Instant updatedAt;
}
