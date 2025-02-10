package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelServiceFindDTO {

    private UUID id;
    private String name;
    private String description;
    private Instant recentTime;
    private List<UUID> ids;
}
