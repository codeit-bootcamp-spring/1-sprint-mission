package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> storage = new HashMap<>();

    @Override
    public Message save(Message message) {
        storage.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
