package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void messageSave(Message message);
    Optional<Message> messageList(UUID id);
    List<Message> messageAllList();
    void updateMessage(Message message);
    void deleteMessage(UUID id);
}
