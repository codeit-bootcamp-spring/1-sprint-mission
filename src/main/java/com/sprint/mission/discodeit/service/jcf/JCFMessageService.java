package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Message updatedMessage) {
        if (data.containsKey(id)) {
            data.put(id, updatedMessage);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
