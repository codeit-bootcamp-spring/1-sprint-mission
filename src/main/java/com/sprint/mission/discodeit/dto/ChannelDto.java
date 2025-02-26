package com.sprint.mission.discodeit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChannelDto {
    private String id;
    private String name;
    private String description;
    private String userId;
    private String channelId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private String type;

}