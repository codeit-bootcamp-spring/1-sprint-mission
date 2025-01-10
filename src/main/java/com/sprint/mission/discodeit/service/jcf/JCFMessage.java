package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessage implements MessageService {
    private final List<Message> messages = new ArrayList<>();
    private final JCFChannel jcfChannel;

    public JCFMessage(JCFChannel jcfChannel) {
        this.jcfChannel = jcfChannel;
    }

    @Override
    public Message createMessage(UUID authorID, String text) {
        Message newMessage = new Message(text, authorID);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public Message createMessage(UUID authorID, UUID channelID, String text) {
        Message newMessage = new Message(text, authorID);
        if(jcfChannel.getChannel(channelID).isEmpty()) {
            return null;
        }
        messages.add(newMessage);
        jcfChannel.addMessageToChannel(channelID, newMessage);
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
