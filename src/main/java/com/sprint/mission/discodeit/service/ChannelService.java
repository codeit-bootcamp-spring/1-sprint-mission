package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // 공개 채널 생성
  ChannelDto createPublic(PublicChannelCreateRequest request);

  // 비공개 채널 생성
  ChannelDto createPrivate(PrivateChannelCreateRequest request);

  // 채널 삭제
  void remove(UUID channelId);

  // 채널 수정
  ChannelDto update(UUID channelId, ChannelModifyRequest request);

  // 유저가 참여 중인 모든 채널 뽑기
  List<ChannelDto> findAllChannelsByUserId(UUID userId);

}
