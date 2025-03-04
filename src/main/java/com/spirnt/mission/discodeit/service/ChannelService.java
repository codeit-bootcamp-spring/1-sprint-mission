package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.enity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // Create
  Channel createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest);    // 채널 생성

  Channel createChannelPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);    // 채널 생성

  // Read
  ChannelDto find(UUID userId, UUID channelId);  // 채널 정보 단건 조회

  List<ChannelDto> findAllByUserId(UUID userId); // 전체 채널 다건 조회

  // Update
  Channel update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest);

  // Delete
  void delete(UUID channelId);    // 채널 삭제

}
