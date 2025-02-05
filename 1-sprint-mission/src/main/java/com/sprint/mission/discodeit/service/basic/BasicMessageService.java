package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;


    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }
    @Override
    public Message create(User user, Channel channel, String content) {
        try {
            Message newMessage = new Message(user, channel, content);
            if(!messageRepository.existsByUser(user)) {
                throw new IllegalArgumentException("User not found");
            } else if (!messageRepository.existsByChannel(channel)) {
                throw new IllegalArgumentException("Channel not found");
            }
            messageRepository.save(newMessage);
            System.out.println("Message created >>> "+ channel.getName()+ " / " +
                    user.getEmail() + " : " + content);
            return newMessage;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Message find(User user, Channel channel, String content) {
        try {
            if(!messageRepository.existsByChannel(channel)) {
                throw new IllegalArgumentException("Channel not found");
            }
            return messageRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
    public Message update(User user, Channel channel, String content) {
        try {
            Message message= find(user, channel,content );
            message.update(content);
            System.out.println("Message updated: " + channel.getName()+ " / " +
                    user.getEmail() + " : " + content);
            messageRepository.save(message);
            return message;
        }catch (IllegalArgumentException e){
            System.out.println("Failed to update message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(User user, Channel channel, Message message) {
        try {
            if(!messageRepository.existsByUser(user)) {
                throw new IllegalArgumentException("User not found");
            }
            if(!messageRepository.existsByChannel(channel)) {
                throw new IllegalArgumentException("Channel not found");
            }
            messageRepository.deleteByMessage(message);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete message: " + e.getMessage());
        }
    }
}
