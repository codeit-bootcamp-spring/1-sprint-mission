package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public record ChannelGetterDto(
        UUID id,
        String channelName,
        ArrayList<UUID>members,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String description
) {
    public static ChannelGetterDto from(Channel channel) {
        return new ChannelGetterDto(channel.getId(), channel.getChannelName(), channel.getMembers(), channel.getCreatedAt(), channel.getUpdatedAt(), channel.getType(), channel.getDescription());
    }

    //todo 겟 멤버스 메서드를 반환하면 채널에 속해있는 유저들의 객체가 그대로 반환된다. 보안 ㄱㅊ?
    //todo Dto만들때 어떤건 특정속성 제외하고 보내고싶다면 어떻게 구별하지 ? (채널유저메세지 전부 해당)
    //todo Dto만들때 게터Dto랑 세터Dto랑 레코드클래스 분리해서만드는게낫나? 아님 걍 하나에 통합? (채널유저메세지 전부 해당)
}
