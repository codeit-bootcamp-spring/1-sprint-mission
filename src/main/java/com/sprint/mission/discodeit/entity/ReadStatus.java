package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class ReadStatus implements Serializable {
    private String userId;
    private String channelId;
    private Instant lastReadTime;

    public ReadStatus(String userId, String channelId, Instant lastReadTime) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = lastReadTime;
    }
}