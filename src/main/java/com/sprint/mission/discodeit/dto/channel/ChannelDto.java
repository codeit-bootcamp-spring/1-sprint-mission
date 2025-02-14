package com.sprint.mission.discodeit.dto.channel;

import lombok.Value;

@Value(staticConstructor = "of")
public class ChannelDto {
    String name;
    String description;
}
