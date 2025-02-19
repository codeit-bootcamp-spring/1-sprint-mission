package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface MessageRepository {
    void save(Message message);

    Message findByMessageId(UUID messageId);

    List<Message> findBySenderId(UUID senderId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findAll();

    void deleteById(UUID messageId);
}
