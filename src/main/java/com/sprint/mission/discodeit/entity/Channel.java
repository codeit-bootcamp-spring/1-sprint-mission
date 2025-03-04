package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.type = type;
        this.name = name;
        this.description = description;
    }

    public void updateUpdatedAt() {
        updatedAt = Instant.now();
    }

    public void update(String name, String description) {
        boolean updated = false;
        if (updateName(name)) {
            updated = true;
        }
        if (updatedescription(description)) {
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

    public boolean updatedescription(String description) {
        if (this.description.equals(description)) {
            return false;
        }
        this.description = description;
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                name + " | " + description + System.lineSeparator()
        );
    }
}
