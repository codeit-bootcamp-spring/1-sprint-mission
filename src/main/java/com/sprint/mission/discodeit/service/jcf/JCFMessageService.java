package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void createMessage(Message message) {
        data.put(message.getId(), message);
        System.out.println("[Message Created]: " + message);
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        Optional<Message> message = Optional.ofNullable(data.get(id));
        System.out.println("[Message Retrieved]: " + message.orElse(null));
        return message;
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>(data.values());
        System.out.println("[All Messages Retrieved]: " + messages);
        return messages;
    }

    @Override
    public void updateMessage(Message message) {
        if (data.containsKey(message.getId())) {
            data.put(message.getId(), message);
            System.out.println("[Message Updated]: " + message);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        Message removedMessage = data.remove(id);
        System.out.println("[Message Deleted]: " + removedMessage);
    }
}
