package some_path._1sprintmission.discodeit.service.jcf;


import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.jcf.JCFMessageRepository;
import some_path._1sprintmission.discodeit.service.ChannelService;
import some_path._1sprintmission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final JCFMessageRepository messageRepository;

    public JCFMessageService(Map<UUID, Channel> channelData) {
        this.messageRepository = new JCFMessageRepository(channelData);
    }

    @Override
    public void create(Channel channel, User sender, Message message) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel does not exist.");
        }

        if (!channel.getMembers().contains(sender)) {
            throw new IllegalArgumentException("Sender is not a member of the channel.");
        }

        messageRepository.save(channel, message); // Delegate to the repository
    }

    @Override
    public Optional<Message> read(UUID id) {
        return messageRepository.findById(id); // Delegate to the repository
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll(); // Delegate to the repository
    }

    @Override
    public void update(UUID id, Message updatedMessage) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message does not exist."));

        if (!existingMessage.getSender().equals(updatedMessage.getSender())) {
            throw new IllegalStateException("Only the original sender can update this message.");
        }

        messageRepository.update(existingMessage, updatedMessage); // Delegate to the repository
    }

    @Override
    public void delete(UUID id) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message does not exist."));

        if (!existingMessage.getSender().equals(existingMessage.getSender())) {
            throw new IllegalStateException("Only the original sender can delete this message.");
        }

        messageRepository.delete(existingMessage); // Delegate to the repository
    }
}