package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(Message message) {
        messageRepository.save(message);
        System.out.println("Message created: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
    }

    @Override
    public Message readMessage(String messageUuid) {
        return messageRepository.findByUuid(messageUuid);
    }

    @Override
    public List<Message> readAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(String messageUuid, String newMessageText) {
        messageRepository.delete(messageUuid);
        messageRepository.save(new Message(messageUuid, newMessageText));
        System.out.println("Message updated: " + newMessageText + " (UUID: " + messageUuid + ")");
    }

    @Override
    public void deleteMessage(String messageUuid) {
        messageRepository.delete(messageUuid);
        System.out.println("Message deleted: UUID " + messageUuid);
    }
}
