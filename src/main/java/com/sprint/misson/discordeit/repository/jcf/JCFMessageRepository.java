package com.sprint.misson.discordeit.repository.jcf;

import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private final HashMap<String, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public boolean delete(Message message) {
        return data.remove(message.getId())!= null;
    }

    @Override
    public Message findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }
}
