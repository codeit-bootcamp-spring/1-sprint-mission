package com.sprint.mission.discodeit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class ChannelDto {
    private UUID id;
    private String name;
    private String description;
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private String type;

}