package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String userId;
    private final String channelId;
    private Instant lastReadAt;

    public ReadStatus(String userId, String channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastRead(Instant timestamp) {
        this.lastReadAt = timestamp;
        update();
    }
}
