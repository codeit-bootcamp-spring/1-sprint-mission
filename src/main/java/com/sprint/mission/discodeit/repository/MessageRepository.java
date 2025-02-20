package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message createMessage(UUID id, Message message);
    void updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
    void deleteMessageByChannelId(UUID channelId);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    Optional<Message> findAllByChannelId(UUID channelId);
}
