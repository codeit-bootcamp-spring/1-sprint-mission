package com.sprint.misson.discordeit.service.basic;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.repository.MessageRepository;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;

import java.util.List;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }


    @Override
    public Message create(String content, String channelId, String userId) throws CustomException {
        if (content == null || content.isEmpty()) {
            System.out.println("Message content is empty for User: " + userId + " Channel: " + channelId);
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        try {
            User userByUUID = userService.getUserByUUID(userId);
            Channel channelByUUID = channelService.getChannelByUUID(channelId);
            if (!channelService.isUserInChannel(channelByUUID, userByUUID)) {
                System.out.println("User with id " + userByUUID.getId() + " not found in this channel.");
                throw new CustomException(ErrorCode.USER_NOT_IN_CHANNEL);
            }
            Message message = new Message(userByUUID, content, channelByUUID);
            return messageRepository.save(message);
        } catch (CustomException e) {
            System.out.println("Failed to create message. User: " + userId + " Channel: " + channelId + " Content: " + content);
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                throw e;
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                throw e;
            }
            throw new CustomException(e.getErrorCode(), "Create message failed");
        }

    }

    @Override
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageByUUID(String messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> getMessageByContent(String content) {
        return messageRepository.findAll().stream().filter(m -> m.getContent().contains(content)).toList();
    }

    @Override
    public List<Message> getMessageBySender(User sender) {
        //여기 고민해보자
        return messageRepository.findAll().stream().filter(m -> m.getSenderId().equals(sender.getId())).toList();
    }

    @Override
    public List<Message> getMessageByCreatedAt(Long createdAt) {
        return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().longValue() == createdAt).toList();
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channel.getId())).toList();
    }

    @Override
    public Message updateMessage(String messageId, String newContent) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (newContent.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        if (!message.getContent().equals(newContent)) {
            message.setContent(newContent);
            message.setUpdatedAt();
        }
        return messageRepository.save(message);
    }

    @Override
    public boolean deleteMessage(Message message) throws CustomException {
        Message messageByUUID = messageRepository.findById(message.getId().toString());
        if (messageByUUID == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        return messageRepository.delete(message);
    }
}
