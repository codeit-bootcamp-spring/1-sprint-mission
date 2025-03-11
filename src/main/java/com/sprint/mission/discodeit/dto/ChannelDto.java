package com.sprint.mission.discodeit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class ChannelDto {
    private UUID id;
    private String name;
    private String description;
    private UUID userId;
    private UUID channelId;

    @Enumerated(EnumType.STRING)
    private String type;

}