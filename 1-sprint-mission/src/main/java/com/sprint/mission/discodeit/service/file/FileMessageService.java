package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMessageService implements MessageService {
    private static FileMessageService instance;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public FileMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public static synchronized FileMessageService getInstance(MessageRepository messageRepository, UserService userService){
        if(instance ==null){
            instance = new FileMessageService(messageRepository ,userService);
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        if (userService.readUser(message.getUserId()) == null) {
            System.out.println("Cannot create message. User with ID " + message.getUserId() + " does not exist.");
            return;
        }
        try {
            messageRepository.save(message);
            System.out.println("Channel created >>>> " + message.getUserId() + " : " + message.getMessageText());
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Create User >>>> " + e.getMessage());
        }
    }

    @Override
    public Message readMessage(String messageUuid) {
        try {
            return messageRepository.findByUuid(messageUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Search User >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Message> readAllMessages() {
        List<Message>messages =  messageRepository.findAll();
        if(messages.isEmpty()){
            System.out.println("   No message found ");
        }
        return messages;
    }

    @Override
    public void updateMessage(String messageUuid, String newMessageText) {
        try {
            Message message = messageRepository.findByUuid(messageUuid);
            message.setMessageText(newMessageText);
            messageRepository.delete(messageUuid);
            messageRepository.save(message);
            System.out.println("Message updated: " + message.getMessageText() + " (User ID: " + message.getUserId() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update Message >>>> " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(String messageUuid) {
        try {
            messageRepository.delete(messageUuid);
            System.out.println("Message with UUID " + messageUuid + " deleted.");

        }catch (IllegalArgumentException e){
            System.out.println("Failed to Delete Message >>>> " + e.getMessage());
        }
    }



}
