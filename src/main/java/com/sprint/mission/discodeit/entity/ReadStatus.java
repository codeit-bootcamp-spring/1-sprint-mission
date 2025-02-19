package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private User user;
    private Channel channel;
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void updateReadTime(Instant newReadTime) {
        this.lastReadAt = newReadTime;
        this.updatedAt = Instant.now();
    }
}
