package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {

    Long save(Message message);

    Message load(Long id);

    Map.Entry<Long, Message> load(UUID uuid);

    Map<Long, Message> loadAll();

    Long delete(Long id);

    void update(Long id, Message message);

    // 조건 달린 상태
    Map<Long, Message> findMessagesByChannelId(UUID uuid);

    void deleteAllByChannelId(UUID channelId);
}
