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
    public Message save(String content, User sender, UUID channelId) {
        Message message = new Message(content, sender, channelId);
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
    public void update(Message message, String content) {
        message.updateContent(content);
    }

    @Override
    public void delete(Message message) {
        messages.remove(message.getId());
    }
}
