package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    List<Message> messages = new ArrayList<>();

    @Override
    public void createMessage(User sender, String content, MessageType type) {
        Message message = new Message(sender, content, type);
        messages.add(message);
    }

    @Override
    public Message findMessage(UUID id) {
        Message message = messages.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);

        return message;
    }

    @Override
    public List<Message> findAllMessages() {
        return messages;
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        messages.stream().filter(m -> m.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(message -> message.updateContent(newContent), () -> System.out.println("message not found"));
    }

    @Override
    public void removeMessage(UUID id) {
        messages.stream().filter(m -> m.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(messages::remove, () -> System.out.println("message not found"));
    }
}
