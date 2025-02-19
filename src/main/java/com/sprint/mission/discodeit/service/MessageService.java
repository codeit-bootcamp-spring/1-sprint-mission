package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(UUID userId, UUID channelId, String content);

    Message getMessageById(UUID messageId);

    List<Message> getAllMessages();

    List<Message> getMessagesByChannel(UUID channelId);

    List<Message> getMessagesBySender(UUID senderId);

    Message updateMessageContent(UUID messageId, String newContent);

    boolean deleteMessage(UUID messageId);
}
