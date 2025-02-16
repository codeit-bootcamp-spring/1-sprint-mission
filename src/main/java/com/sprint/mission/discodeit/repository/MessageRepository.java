package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findAll();

    Optional<Instant> findRecentMessageByChannelId(UUID channelId);

    boolean delete(UUID id);

    void deleteAllByChannelId(UUID channelId);
}

