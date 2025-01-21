package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {
    void createMessage(Message message);

    Message readMessage(String id);

    void updateMessage(Message message);

    void deleteMessage(String id);
}
