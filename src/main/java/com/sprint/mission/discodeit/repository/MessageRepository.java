package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message saveMessage(Message message);

    Message findMessageById(UUID messageId);

    List<Message> findAllMessages();

    List<Message> findAllMessagesByChannel(Channel channel);

    Optional<Message> findLastMessage();

    void removeMessage(UUID messageId);
}
