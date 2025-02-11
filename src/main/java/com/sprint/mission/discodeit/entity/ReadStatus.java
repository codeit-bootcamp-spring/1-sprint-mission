package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 15L;
    private final UUID userId;
    private final UUID channelId;


    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    public void update() {
        updateTimeStamp();
    }

    public boolean isRead(Instant messageSentTime) {
        return !getUpdatedAt().isAfter(messageSentTime);
    }

}
