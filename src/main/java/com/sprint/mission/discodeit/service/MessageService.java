package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(Message message);
    Message create(String content, UUID channelId, UUID authorId);
    Message getMessage(UUID id);
    List<Message> getAllMessages();
    void updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
}
