package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class UserStatus extends BaseEntity {
    private Onoff onoff;
    private final Instant attendAt;
    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.attendAt = Instant.now();
    }

    public boolean online() {
        if(Instant.now().compareTo(attendAt) < 3000) {
            return onoff == Onoff.ONLINE;
        }
        return onoff == Onoff.OFFLINE;
    }

}
