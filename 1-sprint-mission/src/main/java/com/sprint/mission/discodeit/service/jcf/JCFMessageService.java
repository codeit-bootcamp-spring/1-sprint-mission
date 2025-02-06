package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
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
    public Message create(User user, Channel channel, String content) {
        if(!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }
        if(!channelRepository.existsByUser(channel.getUser())) {
            throw new IllegalArgumentException("Channel not found");
        }
        Message newMessage = new Message(user, channel, content);
        messageRepository.save(newMessage);

        System.out.println("Message created >>> " + channel.getName() + " / " +
                user.getEmail() + " : " + content);
        return newMessage;
    }

    @Override
    public Message find(User user) {
        try {
            return messageRepository.findAll().stream()
                    .filter(message -> message.getUser().equals(user))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(user + " not exists"));
        }catch (IllegalArgumentException e){
            System.out.println("Failed to read message11: " + e.getMessage());
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
    public Message update(User user, Channel channel, String newContent) {
        try {
            if(!userRepository.existsById(user.getId())){
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getUser())){
                throw new IllegalArgumentException("Channel not found");
            }
            Message messageToUpdate = messageRepository.findAll().stream()
                    .filter(message
                            -> message.getUser().equals(user) && message.getChannel().equals(channel))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Message not found!!"));

            messageToUpdate.update(newContent);
            System.out.println("Message updated: " + channel.getName() + " / " +
                    user.getEmail() + " : " + newContent);

            return messageToUpdate;

        }catch (IllegalArgumentException e){
            System.out.println("Failed to update message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(User user, Channel channel, Message message) {
        try {
            if(!userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getUser())) {
                throw new IllegalArgumentException("Channel not found");
            }
            System.out.println("Message delete : " + find(user));
            messageRepository.deleteByMessage(message);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete message: " + e.getMessage());
        }
    }



}
