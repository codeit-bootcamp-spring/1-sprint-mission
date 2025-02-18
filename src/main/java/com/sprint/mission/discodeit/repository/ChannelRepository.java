package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    // 저장
    void save(Channel channel);

    // 불러오기
    Map<UUID, Channel> load();

    // 삭제
    void delete(UUID id);
}
