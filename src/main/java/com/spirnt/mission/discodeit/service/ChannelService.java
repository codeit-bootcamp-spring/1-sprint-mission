package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.enity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // Create
    Channel createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest);    // 채널 생성

    Channel createChannelPrivate(PrivateChannelRequest privateChannelCreateRequest);    // 채널 생성

    // Read
    ChannelResponse find(UUID userId, UUID channelId);  // 채널 정보 단건 조회

    List<ChannelResponse> findAllByUserId(UUID userId); // 전체 채널 다건 조회

    // Update
    Channel update(UUID channelId, ChannelUpdateRequest channelUpdateRequest);

    // Delete
    void delete(UUID channelId);    // 채널 삭제

}
