package com.sprint.mission.discodeit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChannelDTO {
    private String id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private String type;

}