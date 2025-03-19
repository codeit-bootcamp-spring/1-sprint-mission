package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성
    ChannelResponseDto createPublicChannel(PublicChannelRequestDto channelReqDTO);

    ChannelResponseDto createPrivateChannel(PrivateChannelRequestDto channelReqDTO);

    // 채널 조회
    // 특정 채널(단건)
    ChannelResponseDto find(UUID uuid);

    // 모든 채널
    List<ChannelResponseDto> findAllByUserId(UUID userId);

    // 채널 수정
    ChannelResponseDto update(UUID channelId, ChannelUpdateDto updateDTO);

    // 채널 삭제
    ChannelResponseDto delete(UUID channelId);
}