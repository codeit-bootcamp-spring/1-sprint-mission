package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

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
    public List<Message> getMessageBySenderId(String senderId) {
        //여기 고민해보자
        return messageRepository.findAll().stream().filter(m -> m.getSenderId().equals(senderId)).toList();
    }

    @Override
    public List<Message> getMessageByCreatedAt(Instant createdAt) {
        return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals( createdAt)).toList();
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channel.getId())).toList();
    }

    @Override
    public Message updateMessage(String messageId, String newContent, Instant updatedAt) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (newContent.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        if (!message.getContent().equals(newContent)) {
            message.setContent(newContent);
            message.setUpdatedAt(updatedAt);
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
