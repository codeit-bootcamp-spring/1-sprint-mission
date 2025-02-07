package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findByMessageId(UUID messageId);
    List<Message> findByChannelId(UUID channelId);
    void deleteByMessageId(UUID messageId);
    void deleteByChannelId(UUID channelId);
}
