package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final ChannelType type;
    private String name;
    private String introduction;

    private List<UUID> participants;

    public Channel(ChannelType type, String name, String introduction) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.type = type;
        this.name = name;
        this.introduction = introduction;
    }

    public Channel(ChannelType type, List<UUID> participants) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.type = type;
        this.participants = participants;
    }

    public void updateUpdatedAt() {
        updatedAt = Instant.now();
    }

    public void update(String name, String introduction) {
        boolean updated = false;
        if (updateName(name)) {
            updated = true;
        }
        if (updateIntroduction(introduction)) {
            updated = true;
        }

        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateIntroduction(String introduction) {
        if (this.introduction.equals(introduction)) {
            return false;
        }
        this.introduction = introduction;
        return true;
    }

    public boolean containsUser(UUID userId) {
        return this.participants.contains(userId);
    }

    @Override
    public String toString() {
        return String.format(
                name + " | " + introduction + System.lineSeparator()
        );
    }
}
