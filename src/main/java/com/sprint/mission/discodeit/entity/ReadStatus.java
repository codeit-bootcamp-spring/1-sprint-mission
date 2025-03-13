package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private final UUID userId;      // 사용자 id

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private final UUID channelId;   // 대상 채널 id

    @Column(name = "last_read_at")
    private Instant lastReadAt;   // 마지막으로 메시지를 읽은 시간

    public ReadStatus(UUID userId, UUID channelId) {

        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadTime() {
        this.lastReadAt = Instant.now();
    }
}
