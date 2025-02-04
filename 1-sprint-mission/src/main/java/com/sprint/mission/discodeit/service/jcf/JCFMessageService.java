package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    public static synchronized JCFMessageService getInstance(MessageRepository messageRepository,UserRepository userRepository, ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new JCFMessageService(messageRepository, userRepository, channelRepository);
        }
        return instance;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            Message newMessage = new Message(content, channelId,authorId);
            if(!channelRepository.existsById(channelId)) {
                throw new IllegalArgumentException("Channel not found");
            } else if (!userRepository.existsById(authorId)) {
                throw new IllegalArgumentException("User not found");
            }
            messageRepository.save(newMessage);
            System.out.println("Message created: " + content);
            return newMessage;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Message find(UUID messageId) {
        try {
            return messageRepository.findById(messageId)
                    .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        }catch (IllegalArgumentException e){
            System.out.println("Failed to read message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Message> findAll() {
        try {
            List<Message> messages = messageRepository.findAll();
            if (messages.isEmpty()) {
                throw new IllegalStateException("No messages found in the system.");
            }
            return messages;
        }catch (IllegalStateException e){
            System.out.println("Failed to read all messages: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        try {
            Message message= find(messageId);
            message.update(newContent);
            System.out.println("Message updated: " + newContent);
            messageRepository.save(message);
            return message;
        }catch (IllegalArgumentException e){
            System.out.println("Failed to update message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(UUID messageId) {
        try {
            messageRepository.deleteById(messageId);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete message: " + e.getMessage());
        }
    }









}
