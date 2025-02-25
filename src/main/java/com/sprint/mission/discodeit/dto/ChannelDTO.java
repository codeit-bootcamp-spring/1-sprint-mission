package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID creatorId;
    private boolean isPrivate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    private List<UUID> memberIds;

    public ChannelDTO(UUID id, String name, String description, UUID creatorId, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.createdAt = Instant.now();
        this.memberIds = List.of();
    }

    @JsonProperty("createdAt")
    public long getCreatedAtAsLong() {
        return createdAt != null ? createdAt.toEpochMilli() : 0;
    }
}
