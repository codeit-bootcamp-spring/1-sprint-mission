package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ResponseChannelDto(
        UUID channelId,
        Channel.ChannelType channelType,
        String chanelName,
        Instant latestMessageTime, // 가장 최근 메시지의 시간 정보
        List<UUID> participantUserIds // PRIVATE 채널일 경우 참여한 사용자 ID 목록
) {
    public static ResponseChannelDto from(Channel channel, Instant latestMessageTime, List<UUID> participantUserIds) {
        return new ResponseChannelDto(
                channel.getId(),
                channel.getChannelType(),
                channel.getChannelName(),
                latestMessageTime,
                participantUserIds
        );
    }
}
