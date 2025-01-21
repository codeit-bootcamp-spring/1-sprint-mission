package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    Map<UUID, Message> messageMap = new HashMap<>();

    @Override
    public Message saveMessage(Message message) {
        return messageMap.put(message.getMessageId(), message);
    }

    @Override
    public void removeMessageById(UUID messageId) {
        messageMap.remove(messageId);
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return messageMap.get(messageId);
    }

    @Override
    public Map<UUID, Message> findAllMessage() {
        return messageMap;
    }
}
