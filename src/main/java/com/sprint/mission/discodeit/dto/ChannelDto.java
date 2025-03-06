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
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private UUID userId;
    private UUID channelId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private String type;

}