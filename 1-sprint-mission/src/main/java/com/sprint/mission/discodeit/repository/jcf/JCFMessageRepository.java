package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<User, List<Message>> messageData;;

    public JCFMessageRepository(Map<User, List<Message>> messageData) {
        this.messageData = messageData;
    }

    @Override
    public Message save(Message message) {
        messageData.computeIfAbsent(message.getUser(), k -> new ArrayList<>()).add(message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageData.values().stream()
                .flatMap(List::stream)
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found Message")));
    }

    @Override
    public List<Message> findAll() {
        List<Message> allMessages = new ArrayList<>();
        messageData.values().forEach(allMessages::addAll);
        return allMessages;
    }


    @Override
    public void deleteByMessage(Message message) {
        List<Message> messages = messageData.get(message.getUser());
        if(messages != null) {
            messages.remove(message);
            if(messages.isEmpty()) {
                messageData.remove(message.getUser());
            }
        }
    }


    @Override
    public boolean existsByChannel(Channel channel) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .anyMatch(m -> m.getChannel().equals(channel));

    }

    @Override
    public boolean existsByUser(User user) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .anyMatch(m -> m.getUser().equals(user));
    }
}
