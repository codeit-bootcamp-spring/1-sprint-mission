package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;

public class FileMessageService implements MessageService {
    private static FileMessageService instance;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public FileMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }


    public static synchronized FileMessageService getInstance(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        if(instance ==null){
            instance = new FileMessageService(messageRepository, channelRepository, userRepository);
        }
        return instance;
    }

    @Override
    public Message create(User user, Channel channel, String content) {
        try {
            if(!userRepository.existsById(user.getId())){
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getUser())){
                throw new IllegalArgumentException("Channel not found");
            }
            Message newMessage = new Message(user, channel, content);
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
            if(channelRepository.existsByUser(user)) {
                throw new IllegalArgumentException("Channel not found");
            }
            return messageRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
    public Message update(User user, Channel channel, String content) {
        try {
            Message message= find(user, channel, content );
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
            if(!userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getUser())) {
                throw new IllegalArgumentException("Channel not found");
            }
            System.out.println("Message delete : " + find(user, channel, message.getContent()));
            messageRepository.deleteByMessage(message);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete message: " + e.getMessage());
        }
    }
}
