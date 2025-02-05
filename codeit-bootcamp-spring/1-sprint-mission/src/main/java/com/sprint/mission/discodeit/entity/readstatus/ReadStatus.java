package com.sprint.mission.discodeit.entity.readstatus;

import com.sprint.mission.discodeit.entity.user.entity.User;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private boolean isRead;
    private final User user;

    public ReadStatus(User user) {
        Objects.requireNonNull(user, "user is null");
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isRead = false;
        this.user = user;
    }

    public void updateReadStatusByReading() {
        this.isRead = true;
        this.updatedAt = Instant.now();
    }

}
