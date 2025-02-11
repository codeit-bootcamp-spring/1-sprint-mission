package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    private UUID id;
    private String name;
    private String description;
    private ChannelType type;
    private Instant createdAt;
    private List<UUID> memberIds;
}
