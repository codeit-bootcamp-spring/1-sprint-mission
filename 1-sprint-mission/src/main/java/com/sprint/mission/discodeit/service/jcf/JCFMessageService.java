package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final MessageRepository messageRepository;
    private final UserService userService;

    private JCFMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public static synchronized JCFMessageService getInstance(MessageRepository messageRepository, UserService userService) {
        if (instance == null) {
            instance = new JCFMessageService(messageRepository, userService);
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        if (userService.readUser(message.getUserId()) == null) {
            System.out.println("Cannot create message. User with ID " + message.getUserId() + " does not exist.");
            return;
        }
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
        Message message = messageRepository.findByUuid(messageUuid);
        if (message != null) {
            message.setMessageText(newMessageText);
            System.out.println("Message updated: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
        } else {
            System.out.println("Message with UUID " + messageUuid + " not found.");
        }
    }

    @Override
    public void deleteMessage(String messageUuid) {
        messageRepository.delete(messageUuid);
        System.out.println("Message deleted: UUID " + messageUuid);
    }
}
