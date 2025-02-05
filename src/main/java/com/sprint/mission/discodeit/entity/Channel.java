package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String introduction;
    private User owner;

    public Channel(String name, String introduction, User owner) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.name = name;
        this.introduction = introduction;
        this.owner = owner;
    }

    public void updateUpdatedAt() {
        updatedAt = Instant.now();
    }

    public void update(String name, String introduction) {
        boolean updated = updateName(name) || updateIntroduction(introduction);
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

    @Override
    public String toString() {
        return String.format(
                name + " | " + introduction + System.lineSeparator()
                        + "Owner: " + owner.getName() + System.lineSeparator()
        );
    }
}
