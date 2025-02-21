package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<String, Message> messageStore = new ConcurrentHashMap<>();

    @Override
    public Message save(Message message) {
        messageStore.put(message.getId(), message);
        return message;
    }

    @Override
    public void deleteById(String id) {
        messageStore.remove(id);
    }

    @Override
    public Optional<Message> findById(String id) {
        return Optional.ofNullable(messageStore.get(id));
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return messageStore.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageStore.values());
    }
}