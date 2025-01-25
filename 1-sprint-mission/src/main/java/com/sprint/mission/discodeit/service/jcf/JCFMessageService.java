package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final MessageRepository messageRepository;


    private JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }

    public static synchronized JCFMessageService getInstance(MessageRepository messageRepository, UserService userService) {
        if (instance == null) {
            instance = new JCFMessageService(messageRepository);
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        try {
            messageRepository.save(message);
            System.out.println("Message created: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Cannot found User. User with ID " + message.getUserId() + " does not exist.");
        }
    }

    @Override
    public Message readMessage(String messageUuid) {
        try {
            return messageRepository.findByUuid(messageUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Read Message >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Message> readAllMessages() {
        try {
            List<Message> messages = messageRepository.findAll();
            if (messages.isEmpty()) {
                throw new IllegalStateException("No messages found in the system.");
            }
            return messages;
        }catch (IllegalStateException e){
            System.out.println("Failed to Read All Messages >>>> " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public void updateMessage(String messageUuid, String newMessageText) {
        try {
            messageRepository.delete(messageUuid);
            messageRepository.save(new Message(messageUuid, newMessageText));
            System.out.println("Message updated: " + newMessageText + " (UUID: " + messageUuid + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update Message >>>> " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(String messageUuid) {
        try {
            messageRepository.delete(messageUuid);
            System.out.println("Message deleted: UUID " + messageUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Delete Message >>>> " + e.getMessage());
        }
    }
}
