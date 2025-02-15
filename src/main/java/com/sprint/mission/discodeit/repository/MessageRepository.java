package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Message find(UUID messageId);
    List<Message> findAll();
    void delete(Message message);
}
