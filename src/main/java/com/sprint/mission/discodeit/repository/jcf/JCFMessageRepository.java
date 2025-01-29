package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

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
    public Message findById(UUID messageId) {
        Message message = this.data.get(messageId);
        return Optional.ofNullable(message).orElseThrow(() -> new NoSuchElementException(messageId + "를 찾을 수 없습니다."));
    }

    @Override
    public void delete(UUID messageId) {
        try {
            if(!data.containsKey(messageId)) throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
            data.remove(messageId);
        } catch (NoSuchElementException e) {
            throw e;
        }

    }

    @Override
    public Map<UUID, Message> findAll() {
        return new HashMap<>(data);
    }
}
