package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {// 생성

  // 공개 채널 생성
  ChannelDto create(PublicChannelCreateRequest request);

  // 비공개 채널 생성
  ChannelDto create(PrivateChannelCreateRequest request);

  // 채널 단건 검색
  ChannelDto find(UUID channelId);

  // 유저 id를 통핸 채널 다건 검색
  List<ChannelDto> findAllByUserId(UUID userId);

  // 공개 채널 수정
  ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

  // 채널 삭제
  void delete(UUID channelId);
}
