package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType type;

    public enum ChannelType {
        PUBLIC, PRIVATE
    }

    public Channel(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        updateName(name);
        updateDescription(description);
    }

    public void updateName(String name) {
        if (this.name.equals(name)) {
            return;
        }
        this.name = name;
    }

    public void updateDescription(String description) {
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
