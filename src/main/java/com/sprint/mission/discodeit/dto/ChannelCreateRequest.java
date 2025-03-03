package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sprint.mission.discodeit.util.InstantDeserializer;
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
public class ChannelCreateRequest {
    private String name;
    private String description;
    private UUID creatorId;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    private List<UUID> members;

    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    public ChannelCreateRequest(String name, String description, UUID creatorId, boolean isPrivate, List<UUID> members) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.members = members != null ? members : List.of();
        this.createdAt = Instant.now();
    }
}

