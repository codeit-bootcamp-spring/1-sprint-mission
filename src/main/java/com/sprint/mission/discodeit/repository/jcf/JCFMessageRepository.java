package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Message> findByUser(User user) {
        return messageMap.values().stream()
                .filter(message -> message.getWriter().getPhone().equals(user.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByChannel(Channel channel) {
        return messageMap.values().stream()
                .filter(message -> message.getChannel().getName().equals(channel.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Message delete(Message message) {
        messageMap.remove(message.getId());
        return message;
    }
}
