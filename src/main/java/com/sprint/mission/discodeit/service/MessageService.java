package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(String content, UUID channelId, UUID writerId);

    List<Message> getAllMessageList();

    Message searchById(UUID messageId);

    void updateMessage(UUID messageId, String content);

    void deleteMessage(UUID messageId);
}
