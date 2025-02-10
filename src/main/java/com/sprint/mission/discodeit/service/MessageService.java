package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String content);
    Message readMessage(UUID id);
    List<Message> readAllMessage();
    Message updateMessage(UUID msgID, String content);
    void deleteMessage (UUID msgID);


}
