package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest); //채널 생성

  ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest); //채널 생성

  ChannelDto findById(UUID channelId); // 채널 단일 조회

  List<ChannelDto> findAllByUserId(UUID userId); // 모든 채널 조회

  ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest); // 채널 수정

  void deleteById(UUID channelId); // 채널 삭제
}
