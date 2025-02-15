package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages;

    public JCFMessageRepository() {
        this.messages = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return messages.values().stream().toList();
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
