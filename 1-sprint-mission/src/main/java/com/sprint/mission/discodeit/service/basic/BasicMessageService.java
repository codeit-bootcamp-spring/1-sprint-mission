package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void createMessage(Message message) {
        messageRepository.save(message);
        System.out.println("Message created: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
    }

    @Override
    public Message readMessage(String messageUuid) {

         return messageRepository.findByUuid(messageUuid)
                 .orElseThrow(() -> new NoSuchElementException("Message with UUID: " + messageUuid));
    }

    @Override
    public List<Message> readAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(String messageUuid, String newMessageText) {
        Message message = messageRepository.findByUuid(messageUuid)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageUuid + " not found"));
        message.setMessageText(newMessageText);
        System.out.println("Message updated: " + newMessageText + " (UUID: " + messageUuid + ")");
    }

    @Override
    public void deleteMessage(String messageUuid) {
        messageRepository.delete(messageUuid);
        System.out.println("Message deleted: UUID " + messageUuid);
    }
}
