package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 5914585350032631538L;

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final ChannelType type;
    private final String name;
    private final String description;

    public static Channel createChannel(ChannelType type, String name, String description) {
        return new Channel(UUID.randomUUID(), Instant.now(), null, type, name, description);
    }

    public Channel update(String name, String description) {
        if (name == null || description == null) {
            throw new IllegalArgumentException();
        }

        if (name.equals(this.name) && description.equals(this.description)) {
            return this;
        }

        return new Channel(
            this.id,
            this.createdAt,
            Instant.now(),
            this.type,
            name,
            description
        );
    }
}
