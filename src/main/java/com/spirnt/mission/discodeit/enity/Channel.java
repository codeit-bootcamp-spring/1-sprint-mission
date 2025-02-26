package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String description;
    ChannelType type;

    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description) {
        boolean anyValueUpdated = false;
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            anyValueUpdated = true;
        }
        if (description != null && description.equals(this.description)) {
            this.description = description;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updateClass(Instant.now());
        }
    }


    @Override
    public String toString() {
        return "Channel{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                '}';
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Channel channel = (Channel) obj;
        return Objects.equals(this.getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
