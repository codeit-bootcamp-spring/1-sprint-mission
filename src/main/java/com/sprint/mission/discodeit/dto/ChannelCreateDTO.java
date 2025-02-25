package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateDTO {
    private String name;
    private String description;
    private UUID creatorId;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    private List<UUID> members;
}
