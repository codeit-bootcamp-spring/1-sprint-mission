package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID channelId;
    private final UUID userId;
    private Instant lastReadAt;

    public ReadStatus(UUID channelId, UUID userId, Instant lastReadAt){
        super();
        this.channelId = channelId;
        this.userId = userId;
        this.lastReadAt =  lastReadAt;
    }

}
