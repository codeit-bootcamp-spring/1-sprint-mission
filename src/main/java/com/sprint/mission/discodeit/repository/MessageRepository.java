package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Optional<Message> findById(UUID uuid);

    List<Message> findAll();

    List<Message> findByChannelId(UUID channelId);

    Optional<Message> findLatestByChannelId(UUID channelId);

    Message delete(Message message);
}
