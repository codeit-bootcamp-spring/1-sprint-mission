package com.spirnt.mission.discodeit.repository.jcf;

import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public void delete(UUID messageId) {
        try {
            if (!data.containsKey(messageId))
                throw new NoSuchElementException("Message ID: " + messageId + " not found");
            data.remove(messageId);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.of(this.data.get(messageId));
    }

    public Map<UUID, Message> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return data.containsKey(messageId);
    }
}
