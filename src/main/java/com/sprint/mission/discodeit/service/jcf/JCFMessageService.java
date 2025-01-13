package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Message createMessage(UUID id, Long createdAt, Long updatedAt, String content) {
        Message message = new Message(id, createdAt, updatedAt, content);
        data.add(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return data.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateMessage(UUID id, String content, Long updatedAt) {
        Message message = getMessage(id);
        if (message != null) {
            message.update(updatedAt);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.removeIf(message -> message.getId().equals(id));
    }
}
