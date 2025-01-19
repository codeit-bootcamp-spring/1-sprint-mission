package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessage implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<>();
    private final JCFChannel jcfChannel;

    public JCFMessage(JCFChannel jcfChannel) {
        this.jcfChannel = jcfChannel;
    }

    @Override
    public Message createMessage(UUID authorID, String text) {
        Message newMessage = new Message(text, authorID);
        messages.put(newMessage.getId(), newMessage);
        return newMessage;
    }

    @Override
    public Message createMessage(UUID authorID, UUID channelID, String text) {
        Message newMessage = new Message(text, authorID);
        if(jcfChannel.getChannel(channelID).isEmpty()) {
            return null;
        }
        messages.put(newMessage.getId(), newMessage);
        jcfChannel.addMessageToChannel(channelID, newMessage);
        return newMessage;
    }

    @Override
    public Map<UUID, Message> getMessages() {
        return messages;
    }

    @Override
    public Optional<Message> getMessage(UUID uuid) {
        return Optional.ofNullable(messages.get(uuid));
    }

    @Override
    public Optional<Message> updateMessage(UUID uuid, String text) {
        Message updatedMessage = messages.get(uuid);
        if(updatedMessage != null) {
            updatedMessage.updateText(text);
            return Optional.of(updatedMessage);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> deleteMessage(UUID uuid) {
        Message deletedMessage = messages.remove(uuid);
        return Optional.ofNullable(deletedMessage);
    }
}
