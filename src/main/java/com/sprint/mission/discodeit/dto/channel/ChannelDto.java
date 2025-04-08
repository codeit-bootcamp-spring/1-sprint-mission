package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    //채널 id
    UUID id,
    //채널명
    String name,
    //채널 종류 - 공개, 비공개
    ChannelType type,
    //채널 종류 - 음성, 텍스트
    ChannelCategory channelCategory,
    //채널 설명
    String description,
    //채널 생성 시점
    Instant createdAt,
    //최근 메세지의 시간 정보
    Instant lastMessageAt,
    //참여한 유저 정보
    List<UserDto> participants
) {

}
