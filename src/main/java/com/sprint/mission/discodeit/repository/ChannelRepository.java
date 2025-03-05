package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {

    Long save(Channel channel);

    Channel load(Long id);

    Map.Entry<Long, Channel> load(UUID uuid);

    Map<Long, Channel> loadAll();

    void update(Long id, Channel channel);

    void delete(Long id);

    // 조건 달린 조회
    Map<Long, Channel> findChannelsByUserId(UUID userId);

}
