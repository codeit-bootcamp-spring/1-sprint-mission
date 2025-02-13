package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private UUID creatorId;
    private boolean isPrivate;
    private Instant createdAt;
    private List<UUID> members;

    public Channel(UUID id, String name, String description, UUID creatorId, boolean isPrivate) {
        super(id);
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.createdAt = Instant.now();
        this.members = new ArrayList<>();
    }

    public boolean isPublic() {
        return !isPrivate;
    }

    public void updateChannel(String name, String description) {
        if (isPrivate) {
            throw new IllegalArgumentException("Private channels cannot be updated.");
        }
        this.name = name;
        this.description = description;
        setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", isPrivate=" + isPrivate +
                ", createdAt=" + createdAt +
                ", members=" + members +
                '}';
    }
}
