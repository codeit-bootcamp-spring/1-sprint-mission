package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    @Column
    private Boolean notificationEnabled;

    protected ReadStatus(User user, Channel channel, Instant lastReadAt,
            boolean notificationEnabled) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
        this.notificationEnabled = notificationEnabled;
    }

    public static ReadStatus createWithDefaultNotification(User user, Channel channel,
            Instant lastReadAt) {
        boolean defaultNotification = channel.getType() == ChannelType.PRIVATE;
        return new ReadStatus(user, channel, lastReadAt, defaultNotification);
    }

    public void update(Instant newLastReadAt) {
        if (newLastReadAt != null && !newLastReadAt.isAfter(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }
    }

    public void updatedNotificationEnabled(boolean newValue) {
        this.notificationEnabled = newValue;
    }
}
