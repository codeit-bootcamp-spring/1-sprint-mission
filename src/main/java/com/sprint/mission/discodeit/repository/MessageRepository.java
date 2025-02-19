package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    Instant findLastMessageTimeByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);
    List<Message> findByChannelId(UUID channelId);
    void delete(Message message);
}
