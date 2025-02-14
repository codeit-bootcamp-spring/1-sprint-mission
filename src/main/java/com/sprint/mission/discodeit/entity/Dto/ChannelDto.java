package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Type.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String channelName,
        ArrayList<UUID>members,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String description
) {

    //이너클래스 파라미터는 유저에게 입력받을 값.
    public static ChannelDto from(Channel channel) {
        return new ChannelDto(channel.getId(), channel.getChannelName(), channel.getMembers(), channel.getCreatedAt(), channel.getUpdatedAt(), channel.getType(), channel.getDescription());
    }

    public record CreatePublicChannelRequest(String channelName, String description){};

    public record CreatePrivateChannelRequest(String channelName, String description, String ...memberNames){};

    public record AddChannelMemberRequest(String channelName, String memberName){};

    public record DeleteChannelRequest(String Channel){};


    //todo 겟 멤버스 메서드를 반환하면 채널에 속해있는 유저들의 객체가 그대로 반환된다. 보안 ㄱㅊ?
    //todo Dto만들때 어떤건 특정속성 제외하고 보내고싶다면 어떻게 구별하지 ? (채널유저메세지 전부 해당)
    //todo Dto만들때 게터Dto랑 세터Dto랑 레코드클래스 분리해서만드는게낫나? 아님 걍 하나에 통합? (채널유저메세지 전부 해당)
}
