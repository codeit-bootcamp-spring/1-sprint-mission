package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Type.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String channelName,
        List<UUID> members,   // PRIVATE 채널일 경우만 포함
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String description,
        Instant lastMessageAt
) {

    //이너클래스 파라미터는 유저에게 입력받을 값.
    public static ChannelDto from(Channel channel, Optional<Instant> lastMessageAt) {
        return new ChannelDto(channel.getId(),
                channel.getChannelName(),
                channel.getMembers().isEmpty() ? null : channel.getMembers(), // PRIVATE 채널이면 members 포함
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getDescription(),
                lastMessageAt.orElse(null) // 메시지가 없으면 null
        );
    }

    //todo 설명변경 리퀘스트 DTO라고 세분화했는데, 그냥 UpdateDto로 설명뿐만아니라 여러 변경요청을 퉁쳐야하나?
    public record CreatePublicChannelRequest(String channelName, String description){};

    public record CreatePrivateChannelRequest(String channelName, String description, String ...memberNames){};

    public record AddChannelMemberRequest(String channelName, String memberName){};

    public record DeleteChannelRequest(String channelName){};

    public record ChangeDescriptionRequest(String channelName, String description){};
}
//todo Dto만들때 어떤건 특정속성 제외하고 보내고싶다면 어떻게 구별하지 ? (채널유저메세지 전부 해당)
