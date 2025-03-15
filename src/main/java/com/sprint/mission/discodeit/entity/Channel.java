package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseUpdatableEntity {
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }


    public void update(String name, String description) {
        updateName(name);
        updatedescription(description);
    }

    public void updateName(String name) {
        if (this.name.equals(name)) {
            return;
        }
        this.name = name;
    }

    public void updatedescription(String description) {
        if (this.description.equals(description)) {
            return;
        }
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                name + " | " + description + System.lineSeparator()
        );
    }
}
