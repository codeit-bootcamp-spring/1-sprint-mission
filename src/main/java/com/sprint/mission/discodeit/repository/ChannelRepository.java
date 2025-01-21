package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    // 채널 저장
    Channel saveChannel(Channel channel);

    // 채널 삭제
    void removeChannelById(UUID channelId);

    // 채널 조회
    Channel findChannelById(UUID channelId);

    // 모든 채널 조회
    Map<UUID, Channel> findAllChannel();

}
