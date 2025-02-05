package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class FileMessageService extends FileService implements MessageService {

    private final Path messageDirectory;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageDirectory = super.getBaseDirectory().resolve("message");
        init(messageDirectory);
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
            Path newMessagePath = messageDirectory.resolve(message.getId().concat(".ser"));
            save(newMessagePath, message);

            return message;
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
        return FileService.load(messageDirectory);
    }

    @Override
    public Message getMessageByUUID(String messageId) throws CustomException {
        Message message = getMessages().stream().filter(m -> m.getId().equals(messageId)).findFirst().orElse(null);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND, String.format("Message with id %s not found", messageId));
        }
        return message;
    }

    @Override
    public List<Message> getMessageByContent(String content) {
        return getMessages().stream().filter(m -> m.getContent().contains(content)).toList();
    }

    @Override
    public List<Message> getMessageBySenderId(String senderId) {
        return getMessages().stream().filter(m -> m.getSenderId().equals(senderId)).toList();
    }

    @Override
    public List<Message> getMessageByCreatedAt(Instant createdAt) {
        return getMessages().stream().filter(m -> m.getCreatedAt().equals(createdAt)).toList();
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return getMessages().stream().filter(m-> m.getChannelId().equals(channel.getId())).toList();
    }

    @Override
    public Message updateMessage(String messageId, String newContent, Instant updatedAt) {

        Message message = getMessageByUUID(messageId);

        if (message == null) {
            System.out.println("Failed to update message. Message not found.");
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (newContent.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        if (!message.getContent().equals(newContent)) {
            message.setUpdatedAt(updatedAt);
            message.setContent(newContent);
            Path messagePath = messageDirectory.resolve(messageId.concat(".ser"));
            save(messagePath, message);
        }
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) {
        Message msg = getMessageByUUID(message.getId());

        if (msg == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND, String.format("Message with id %s not found", message.getId()));
        }
        Path messagePath = messageDirectory.resolve(message.getId().concat(".ser"));
        return delete(messagePath);
    }
}
