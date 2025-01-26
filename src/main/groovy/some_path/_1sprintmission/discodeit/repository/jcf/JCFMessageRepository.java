package some_path._1sprintmission.discodeit.repository.jcf;


import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;

import java.util.*;

public class JCFMessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();
    private final Map<UUID, Channel> channelData;

    public JCFMessageRepository(Map<UUID, Channel> channelData) {
        this.channelData = channelData;
    }

    // Save a message
    public void save(Channel channel, Message message) {
        messageData.put(message.getId(), message);
        channel.addMessage(message); // Add the message to the channel
    }

    // Find a message by ID
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageData.get(id));
    }

    // Find all messages
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    // Find all messages by channel
    public List<Message> findAllByChannel(UUID channelId) {
        Channel channel = channelData.get(channelId);
        return channel != null ? channel.getMessages() : Collections.emptyList();
    }

    // Delete a message
    public void delete(Message message) {
        messageData.remove(message.getId());
        Channel channel = channelData.get(message.getId());
        if (channel != null) {
            channel.getMessages().remove(message); // Remove the message from the channel
        }
    }

    // Update a message
    public void update(Message oldMessage, Message updatedMessage) {
        messageData.put(oldMessage.getId(), updatedMessage);

        // Update message in the channel
        Channel channel = channelData.get(oldMessage.getId());
        if (channel != null) {
            channel.getMessages().remove(oldMessage);
            channel.getMessages().add(updatedMessage);
        }
    }
}