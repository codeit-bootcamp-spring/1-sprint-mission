package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;

import java.nio.file.Path;
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
    public List<Message> getMessageBySender(User sender) {
        return getMessages().stream().filter(m-> m.getSenderId().equals(sender.getId())).toList();
    }

    @Override
    public List<Message> getMessageByCreatedAt(Long createdAt) {
        return getMessages().stream().filter(m-> m.getCreatedAt().equals(createdAt)).toList();
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return getMessages().stream().filter(m-> m.getChannelId().equals(channel.getId())).toList();
    }

    @Override
    public Message updateMessage(String messageId, String newContent, long updatedAt) {
        //todo - 모든 필드 검사해서 업데이트 해줘야하나?
        //아니, 유저랑 채널 바뀌면 얘도 업데이트해야하는 문제 발생.
        //그럴바엔 그냥 user도 id로 저장하는 방향이 나을 것 같다.

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
