package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Value;

@Value(staticConstructor = "of")
public class ChannelDto {
    ChannelType type;
    String name;
    String description;
}
