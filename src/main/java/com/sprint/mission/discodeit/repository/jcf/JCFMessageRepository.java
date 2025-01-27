package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public Message save(Message message) {
        messageData.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageData.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public void delete(UUID id) {
        messageData.remove(id);
    }
}
