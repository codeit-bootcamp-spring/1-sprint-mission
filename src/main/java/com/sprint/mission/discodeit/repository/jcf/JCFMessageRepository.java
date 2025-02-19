package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


import java.util.*;

@Profile("jcf")
@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageMap;

    public JCFMessageRepository() {
        this.messageMap = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        return messageMap.values().stream()
                .filter(message -> message.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream().toList();
    }

    @Override
    public Message delete(Message message) {
        messageMap.remove(message.getId());
        return message;
    }
}
