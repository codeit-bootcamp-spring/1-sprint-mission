package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity {
    // TODO: channelId, userId 변수 및 관련 로직 제거
    private UUID channelId;
    private UUID userId;

    private User user;
    private Channel channel;
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        if (lastReadAt != null && !lastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = lastReadAt;
        }
    }

    public boolean isSameChannelId(UUID channelId) {
        return this.channelId.equals(channelId);
    }

    public boolean isSameUserId(UUID userId) {
        return this.userId.equals(userId);
    }
}
