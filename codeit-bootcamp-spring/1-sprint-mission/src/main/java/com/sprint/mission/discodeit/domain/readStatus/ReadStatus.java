package com.sprint.mission.discodeit.domain.readStatus;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ReadStatus {

    private final UUID id;
    private final User user;
    private final Channel channel;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel) {
        validate(user, channel);
        id = UUID.randomUUID();
        this.user = user;
        this.channel = channel;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        this.updatedAt = lastReadAt;
    }

    private void validate(User user, Channel channel) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("user is null");
        }
        if (Objects.isNull(channel)) {
            throw new IllegalArgumentException("channel is null");
        }
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }
}
