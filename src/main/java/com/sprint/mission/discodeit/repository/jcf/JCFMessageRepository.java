package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new ConcurrentHashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Instant> findRecentMessageByChannelId(UUID channelId) {
        return Optional.empty();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {

    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
