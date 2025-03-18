package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "read_statuses")
@Getter
@Builder
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity{
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "channel_id")
    private UUID channelId;

    @Column(name = "last_read_at")
    private Instant lastRead_at;

    protected ReadStatus() { }

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastRead_at = null;
    }

    public void markAsRead() {
        this.lastRead_at = Instant.ofEpochMilli(System.currentTimeMillis());
    }
}
