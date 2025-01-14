package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    // 저장
    void saveChannel(Channel channel);
    // 읽기
    Channel findChannelById(UUID id);
    Map<UUID, Channel> getAllChannels();
    // 삭제
    void deleteAllChannels();
    void deleteChannelById(UUID id);

}
