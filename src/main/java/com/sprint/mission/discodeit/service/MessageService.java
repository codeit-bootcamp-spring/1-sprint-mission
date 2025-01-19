package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.UUID;


public interface MessageService {
    HashMap<UUID, Message> getMessageMap();

    UUID sendMessage(UUID fromUserId, UUID channelId, String content);

    Message getMessage(UUID messageId);

    boolean reviseMessage(UUID messageId, String content);

    boolean deleteMessage(UUID messageId);

    boolean isMessageExist(UUID messageId);

    String getMessageContent(UUID messageId);
}
