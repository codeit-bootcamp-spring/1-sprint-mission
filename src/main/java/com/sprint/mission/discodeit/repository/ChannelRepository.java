package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {

  // 채널 저장
  Channel saveChannel(Channel channel);

  // 채널 삭제
  void removeChannelById(UUID channelId);

  // 채널 조회
  Channel findChannelById(UUID channelId);

  // 채널 이름으로 조회
  default Channel findChannelByName(String channelName) {
    return null;
  }


  // 모든 채널 조회
  Map<UUID, Channel> findAllChannel();

  // 데이터 초기화 메서드 추가
  void clearData();

  void resetData();

  List<Channel> findAllChannelById(UUID channelId);
}
