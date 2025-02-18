package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> storage = new ConcurrentHashMap<>();

    @Override
    public Message save(Message message) {
        storage.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return storage.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
