package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  @Transactional
  ChannelDto createPublicChannel(ChannelPublicRequest channelPublicRequest);

  @Transactional
  ChannelDto createPrivateChannel(ChannelPrivateRequest channelPrivateRequest);

  // Read : 전체 채널 조회, 특정 채널 조회
  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto getChannelById(UUID id);

  // Update : 특정 채널 이름 변경
  @Transactional
  ChannelDto updateChannel(UUID id, ChannelUpdateRequest channelUpdateRequest);

  // Delete : 특정 채널 삭제
  @Transactional
  void deleteChannelById(UUID id);
}
