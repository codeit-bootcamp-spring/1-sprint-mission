package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    Map<UUID, Message> messageMap = new HashMap<>();

    @Override
    public Message saveMessage(Message message) {
        messageMap.put(message.getId(), message);

        return messageMap.get(message.getId());
    }

    @Override
    public void removeMessageById(UUID messageId) {
        Message findMessage = findMessageById(messageId);
        messageMap.remove(findMessage.getId());
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
