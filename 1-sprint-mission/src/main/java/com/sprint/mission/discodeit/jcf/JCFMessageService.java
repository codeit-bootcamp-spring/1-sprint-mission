package com.sprint.mission.discodeit.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final List<Message> messageList;
    private final UserService userService;

    public JCFMessageService(UserService userService) {
        this.userService = userService;
        this.messageList = new ArrayList<>();
    }

    public static synchronized JCFMessageService getInstance(UserService userService){
        if(instance ==null){
            instance = new JCFMessageService(userService);
        }
        return instance;
    }



    @Override
    public void createMessage(Message message) {
        for (Message existingMessage : messageList) {
            if (existingMessage.getMessageUuid().equals(message.getMessageUuid())) {
                System.out.println("Message with UUID " + message.getMessageUuid() + " already exists.");
                return;
            }
        }
        messageList.add(message);
        System.out.println("Message created: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
    }

    @Override
    public Message readMessage(String messageUuid) {
        return messageList.stream()
                .filter(message -> message.getMessageUuid().toString().equals(messageUuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> readAllMessages() {
        return new ArrayList<>(messageList);
    }

    @Override
    public void updateMessage(String messageUuid, String newMessageText) {
        Message message = readMessage(messageUuid);
        if (message != null && message.getMessageUuid() != null) {
            message.setMessageText(newMessageText);
            System.out.println("Message updated: " + message.getMessageText() + " (UUID: " + message.getMessageUuid() + ")");
        } else {
            System.out.println("Message with UUID " + messageUuid + " not found.");
        }
    }

    @Override
    public void deleteMessage(String messageUuid) {
        Message message = readMessage(messageUuid);
        if (message != null) {
            messageList.remove(message);
            System.out.println("Message with UUID " + messageUuid + " deleted.");
        } else {
            System.out.println("Message with UUID " + messageUuid + " not found.");
        }
    }
}
