package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public void addMessage(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message getMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = data.get(id);
        if (message != null) {
            message.updateContent(newContent);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
