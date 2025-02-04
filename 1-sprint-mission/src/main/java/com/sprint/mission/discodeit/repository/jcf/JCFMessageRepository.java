package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        this.data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        boolean exists = this.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("Message not found");
        }
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = this.data.values().stream().toList();
        if(messages.isEmpty()) {
            System.out.println("No message found");
            return Collections.emptyList();
        }
        return messages;

    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        boolean exists = this.existsById(id);
        if(!exists) {
            throw new IllegalArgumentException("Message not found");
        }
        System.out.println("Message deleted: " + id);
        this.data.remove(id);
    }
}
