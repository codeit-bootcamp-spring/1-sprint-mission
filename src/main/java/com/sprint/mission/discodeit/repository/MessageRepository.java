package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(UUID messageId);

    List<Message> findByChannelId(UUID channelId);

    void updateMessage(Message message);

    void deleteMessage(UUID messageId);

    void deleteByChannelId(UUID channelId);
}
