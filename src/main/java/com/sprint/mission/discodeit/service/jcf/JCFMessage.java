package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessage implements MessageService {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public Message createMessage(String text) {
        Message newMessage = new Message(text);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public Optional<Message> getMessage(UUID uuid) {
        for (Message message : messages) {
            if (message.getId().equals(uuid)) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> updateMessage(UUID uuid, String text) {
        for (Message message : messages) {
            if (message.getId().equals(uuid)) {
                message.updateText(text);
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> deleteMessage(UUID uuid) {
        for (Message message : messages) {
            if (message.getId().equals(uuid)) {
                messages.remove(message);
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }
}
