package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> getMessageById(UUID id);
    List<Message> getAllMessage();
    boolean existsById(UUID id);
    void deleteMessage(UUID id);

    Optional<Instant> findLastMessageTimeByChannelId(UUID id);
    List<Message> getMessagesByChannelId(UUID channelId);

    void deleteByChannelId(UUID id);
}
