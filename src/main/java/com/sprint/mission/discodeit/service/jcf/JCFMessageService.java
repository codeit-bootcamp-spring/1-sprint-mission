package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    List<Message> data = new ArrayList<>();

    @Override
    public void createMessage(User sender, String content, MessageType type) {
        Message message = new Message(sender, content, type);
        data.add(message);
    }

    @Override
    public Message findMessage(UUID id) {
        Message message = data.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("message not found"));
        return message;
    }

    @Override
    public List<Message> findAllMessages() {
        return data;
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = findMessage(id);
        message.updateContent(newContent);
    }

    @Override
    public void removeMessage(UUID id) {
        Message message = findMessage(id);
        data.remove(message);
    }
}
