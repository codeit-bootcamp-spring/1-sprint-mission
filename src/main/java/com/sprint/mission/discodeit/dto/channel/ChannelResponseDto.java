package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;

public record ChannelResponseDto(
        //채널 id
        String id,
        //채널명
        String channelName,
        //채널 종류 - 공개, 비공개
        ChannelType channelType,
        //채널 종류 - 음성, 텍스트
        ChannelCategory channelCategory,
        //채널 설명
        String description,
        //최근 메세지의 시간 정보
        Instant lastMessageTimestamp,
        //참여한 유저 정보
        List<String> userIds
) {
    @Override
    public String toString() {
        return "[ChannelResponseDto] {id: " + id + " channelName: " + channelName + " channelType: " + channelType + " channelCategory: " + channelCategory + " description: " + description +
                " lastMessageTimestamp: " + lastMessageTimestamp + " userIds: " + (userIds != null ? userIds.stream().toList() : "Public Channel") + "]";
    }

    public static ChannelResponseDto from(Channel channel, Instant lastMessageTimestamp, List<String> userIds) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getChannelCategory(),
                channel.getDescription(),
                lastMessageTimestamp,
                userIds
        );
    }

}
