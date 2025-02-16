package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

//id, createdAt, updatedAt
//private String name; //채널명
//private String topic; //채널의 목적 규칙에 대한 설명
//private ChannelType type; //채널 타입

@Getter
@AllArgsConstructor
public class CreatePublicChannelRequest {
    private final String name;
    private final String topic;
}
