package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Messages;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessage implements MessageService {
    private final Messages messages = new Messages();
    private final JCFChannel jcfChannel;

    public JCFMessage(JCFChannel jcfChannel) {
        this.jcfChannel = jcfChannel;
    }

    @Override
    public Message createMessage(UUID authorID, UUID channelID, String text) {
        if(jcfChannel.getChannel(channelID).isEmpty()) {
            return null;
        }
        Message newMessage = new Message(text, authorID, channelID);
        messages.add(newMessage.getId(), newMessage);
        jcfChannel.addMessageToChannel(channelID, newMessage.getId());
        return newMessage;
    }

    @Override
    public Map<UUID, Message> getMessages() {
        return messages.asReadOnly();
    }

    @Override
    public Optional<Message> getMessage(UUID uuid) {
        return messages.get(uuid);
    }

    @Override
    public Optional<Message> updateMessage(UUID uuid, String text) {
        return messages.update(uuid, text);
    }

    @Override
    public Optional<Message> deleteMessage(UUID uuid) {
        return messages.remove(uuid);
    }
}
