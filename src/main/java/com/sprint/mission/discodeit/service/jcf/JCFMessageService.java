package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.collection.Messages;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Messages messages = new Messages();
    private final ChannelService channelService;

    public JCFMessageService(ChannelService jcfChannel) {
        this.channelService = jcfChannel;
    }

    @Override
    public Optional<Message> createMessage(UUID authorID, UUID channelID, String text) {
        return channelService.getChannel(channelID)
                .map(channel -> {
                    Message newMessage = new Message(text, authorID, channelID);
                    messages.add(newMessage.getId(), newMessage);
                    channelService.addMessageToChannel(channelID, newMessage.getId());
                    return newMessage;
                });
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
