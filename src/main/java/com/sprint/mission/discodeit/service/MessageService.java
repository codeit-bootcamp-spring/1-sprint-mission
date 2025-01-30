package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.UUID;


public interface MessageService {
    HashMap<UUID, Message> getMessagesMap();

    UUID createMessage(UUID fromUserId, UUID channelId, String content);

    Message getMessageById(UUID messageId);

    boolean reviseMessageContent(UUID messageId, String content);

    boolean deleteMessage(UUID messageId);

    boolean isMessageExist(UUID messageId);

    String getMessageContent(UUID messageId);

    boolean printMessageDetails(UUID messageId);
}
