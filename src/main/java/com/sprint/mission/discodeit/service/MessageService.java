package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID channelId, UUID authorId, String content);

    Message getMessageById(UUID channelId, UUID messageId);

    List<Message> getChannelMessages(UUID channelId);

    void updateMessage(UUID channelId, UUID messageId, String newContent);

    void deleteMessage(UUID channelId, UUID messageId);
}
