package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void messageSave(Message message);
    Optional<Message> findMessage(UUID id);
    List<Message> findAllMessages();
    void updateMessage(UUID id,String updateMessage);
    void deleteMessage(UUID id);
}
