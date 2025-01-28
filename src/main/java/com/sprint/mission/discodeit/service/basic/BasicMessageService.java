package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
    }

    @Override
    public Optional<Message> createMessage(UUID authorID, UUID channelID, String text) {
        return channelService.getChannel(channelID)
                .map(channel -> {
                    Message newMessage = messageRepository.save(new Message(text, authorID, channelID));
                    channelService.addMessageToChannel(channelID, newMessage.getId());
                    return newMessage;
                });
    }

    @Override
    public Map<UUID, Message> getMessages() {
        return messageRepository.getAllMessages().stream()
                .collect(Collectors.toMap(Message::getId, message -> message));
    }

    @Override
    public Optional<Message> getMessage(UUID uuid) {
        return Optional.ofNullable(messageRepository.getMessageById(uuid));
    }

    @Override
    public Optional<Message> updateMessage(UUID uuid, String text) {
        return Optional.ofNullable(messageRepository.getMessageById(uuid))
                .map(message -> {
                    message.updateText(text);
                    return messageRepository.save(message);
                });
    }

    @Override
    public Optional<Message> deleteMessage(UUID uuid) {
        return Optional.ofNullable(messageRepository.getMessageById(uuid))
                .map(message -> {
                    messageRepository.deleteById(uuid);
                    messageRepository.save();
                    return message;
                });
    }
}
