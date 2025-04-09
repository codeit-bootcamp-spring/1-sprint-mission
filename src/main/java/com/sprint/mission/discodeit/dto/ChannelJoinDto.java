package com.sprint.mission.discodeit.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelJoinDto {
    private UUID userId;
    private UUID channelId;
    private String channelName;
}
