package com.sprint.mission.discodeit.dto.channel;

public record CreatePublicChannelDto(
    //채널명
    String channelName,
    //채널설명
    String description
) {

}
