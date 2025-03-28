package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {// 생성

  // 공개 채널 생성
  ChannelDto create(CreatePublicChannelRequest request);

  // 비공개 채널 생성
  ChannelDto create(CreatePrivateChannelRequest request);

  // 채널 단건 검색
  ChannelDto find(UUID channelId);

  // 유저 id를 통핸 채널 다건 검색
  List<ChannelDto> findAllByUserId(UUID userId);

  // 공개 채널 수정
  ChannelDto update(UUID channelId, UpdatePublicChannelRequest request);

  // 채널 삭제
  void delete(UUID channelId);
}
