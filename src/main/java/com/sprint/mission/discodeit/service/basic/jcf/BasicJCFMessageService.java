package com.sprint.mission.discodeit.service.basic.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BasicJCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;

    public BasicJCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void setDependencies(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }

        if (!userService.readUser(message.getAuthor()).isPresent()) {
            throw new IllegalArgumentException("Author does not exist " + message.getAuthor().getId());
        }

        Optional<Channel> existingChannel = channelService.readChannel(message.getChannel());
        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId());
        }

        Channel channel = existingChannel.get();
        if (!channel.getParticipants().contains(message.getAuthor())) {
            throw new IllegalArgumentException("Author is not a participant of the channel: " + message.getChannel().getId());
        }

        Message savedMessage = messageRepository.save(message);
        System.out.println(savedMessage.toString() + "\n메세지 생성 완료\n");
        return savedMessage;
    }

    @Override
    public Optional<Message> readMessage(Message message) {
        Optional<Message> foundMessage = messageRepository.findById(message.getId());
        foundMessage.ifPresent(msg -> System.out.println(msg.toString()));
        return foundMessage;
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateByAuthor(User user, Message updatedMessage) {
        Optional<Message> existingMessage = messageRepository.findAll().stream()
                .filter(message -> message.getAuthor().equals(user))
                .findFirst();

        if (existingMessage.isEmpty()) {
            throw new NoSuchElementException("No message found for the given User");
        }

        Message message = existingMessage.get();
        System.out.println("수정 전 메시지 = " + message.getContent());
        message.updateContent(updatedMessage.getContent());
        message.updateChannel(updatedMessage.getChannel());
        message.updateTime();

        Message savedMessage = messageRepository.save(message);
        System.out.println("수정 후 메시지 = " + savedMessage.getContent());
        return savedMessage;
    }

    @Override
    public boolean deleteMessage(Message message) {
        Optional<Message> existingMessage = messageRepository.findById(message.getId());
        if (existingMessage.isEmpty()) {
            return false;
        }
        messageRepository.delete(message.getId());
        return true;
    }

    @Override
    public void deleteMessageByChannel(Channel channel) {
        messageRepository.deleteByChannel(channel);
    }

    @Override
    public void deleteMessageByUser(User user) {
        messageRepository.deleteByUser(user);
    }
}
