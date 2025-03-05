package com.sprint.mission.discodeit.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelJoinDto {
    private String userId;
    private String channelName;
}
