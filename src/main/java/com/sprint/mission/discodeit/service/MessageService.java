package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String content);
    Message readMessage(UUID msgID);
    List<Message> readAllMessage();
    Message modifyMessage(UUID msgID, String content);
    void deleteMessage (UUID msgID);


}
