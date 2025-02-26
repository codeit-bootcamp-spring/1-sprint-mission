package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface MessageRepository {
    /*UUID createMessage(UUID sender, String content);

    UUID createMessage(UUID id, UUID sender, String content);

    Message getMessage(UUID id);
    List<Message> getMessageByUserId(UUID userId);

    void setMessage(UUID id, String content);

    void initializeMessage(Message message);*/

    UUID save(UUID sender,UUID channelId, String content);
    Message findMessageById(UUID id);
    List<Message> findMessagesBySenderId(UUID id);
    List<Message> findMessagesByChannelId(UUID id);
    List<Message> findAll();
    boolean delete(UUID messageId);
    void update(UUID id, String content);
    void initializeMessage(Message message);
}

