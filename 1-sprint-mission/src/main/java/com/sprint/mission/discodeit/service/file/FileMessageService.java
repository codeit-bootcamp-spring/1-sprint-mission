package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import com.sprint.mission.discodeit.repository.interfacepac.MessageRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

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
    public Message create(MessageCreateDTO messageCreateDTO) {
        if(!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }
        if(!channelRepository.existsByUser(channel.getOwner())) {
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
    public MessageResponseDTO update(MessageUpdateDTO updateDTO) {
        try {
            if(!userRepository.existsById(user.getId())){
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getOwner())){
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
    public void delete(UUID messageId) {
        try {
            if(!userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("User not found");
            }
            if(!channelRepository.existsByUser(channel.getOwner())) {
                throw new IllegalArgumentException("Channel not found");
            }
            System.out.println("Message delete : " + find(user));
            messageRepository.deleteByMessage(messageId);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete message: " + e.getMessage());
        }
    }
}
