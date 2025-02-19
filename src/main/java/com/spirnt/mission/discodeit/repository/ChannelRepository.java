package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.Channel;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    // 데이터베이스 저장
    void save(Channel channel);

    void delete(UUID channelId);

    Optional<Channel> findById(UUID channelId);

    Map<UUID, Channel> findAll();

    // 존재 검증
    boolean existsById(UUID channelId);
}
