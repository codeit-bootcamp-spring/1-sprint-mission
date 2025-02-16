package com.sprint.mission.discodeit.service;


import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelService {
    //생성
    Channel createPublicChannel(CreatePublicChannelDto publicChannelDto);
    Channel createPrivateChannel(CreatePrivateChannelDto privateChannelDto);

    // 수정
    Channel updateChannel(UpdateChannelDto updateChannelDto);

    // 조회
    ChannelResponse findChannel(UUID channelId);
    List<ChannelResponse> findAllChannelsByUserId(UUID userId);

    // 삭제
    void deleteChannel(Channel channel);
}
