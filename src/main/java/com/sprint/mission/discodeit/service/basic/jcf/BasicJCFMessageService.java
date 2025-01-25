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
import java.util.UUID;

public class BasicJCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;

    public BasicJCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }

        // User가 실제로 존재하는지 확인
        User author = userService.readUser(message.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author does not exist: "
                        + message.getAuthor().getId()));
        //Channel이 실제로 존재하는지 확인
        Channel channel = channelService.readChannel(message.getChannel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId()));

        if (!channel.getParticipants().containsKey(message.getAuthor().getId())) {
            throw new IllegalArgumentException("Author is not a participant of the channel: " + message.getChannel().getId());
        }

        Message savedMessage = messageRepository.save(message);
        System.out.println(savedMessage.toString() + "\n메세지 생성 완료\n");
        return savedMessage;
    }

    @Override
    public Optional<Message> readMessage(UUID existMessageId) {
        Optional<Message> existMessage = messageRepository.findById(existMessageId);
        existMessage.ifPresent(msg -> System.out.println(msg.toString()));
        return existMessage;
    }

    @Override
    public List<Message> readAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateByAuthor(UUID existUserId, Message updateMessage) {
        Message existMessage = messageRepository.findAll().stream()
                .filter(message -> message.getAuthor().equals(existUserId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        System.out.println("수정 전 메시지 = " + existMessage.getContent());
        existMessage.updateContent(updateMessage.getContent());
        existMessage.updateChannel(updateMessage.getChannel());
        existMessage.updateTime();

        Message savedMessage = messageRepository.save(existMessage);
        System.out.println("수정 후 메시지 = " + savedMessage.getContent());
        return savedMessage;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        Optional<Message> existingMessage = messageRepository.findById(messageId);
        if (existingMessage.isEmpty()) {
            return false;
        }
        messageRepository.delete(messageId);
        return true;
    }
}
