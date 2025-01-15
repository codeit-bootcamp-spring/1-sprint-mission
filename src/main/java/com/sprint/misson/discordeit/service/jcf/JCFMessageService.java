package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final HashMap<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    //생성
    @Override
    public Message createMessage(User user, String content, Channel channel) throws RuntimeException {

        if (content == null || content.isEmpty()) {
            System.out.println("Message content is empty for User: " + user.getId() + " Channel: " + channel.getId());
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        try {
            User userByUUID = userService.getUserByUUID(user.getId().toString());
            Channel channelByUUID = channelService.getChannelByUUID(channel.getId().toString());
            if (!channelService.isUserInChannel(channelByUUID, userByUUID)) {
                System.out.println("User with id " + userByUUID.getId() + " not found in this channel.");
                throw new CustomException(ErrorCode.USER_NOT_IN_CHANNEL);
            }
            Message message = new Message(userByUUID, content, channelByUUID);
            data.put(message.getId(), message);
            return message;
        } catch (CustomException e) {
            System.out.println("Failed to create message. User: " + user.getId() + " Channel: " + channel.getId() + " Content: " + content);
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                throw new CustomException(e.getErrorCode());
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                throw new CustomException(e.getErrorCode());
            }
            throw new CustomException(e.getErrorCode(), "Create message failed");
        }
    }

    //모두 읽기
    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(data.values());
    }

    //단일 조회 - uuid
    @Override
    public Message getMessageByUUID(String messageId) throws RuntimeException {
        Message message = data.get(UUID.fromString(messageId));
        if (message == null) {
            System.out.println("Message with id " + messageId + " not found");
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        return message;
    }

    //다건 조회 - 내용
    @Override
    public List<Message> getMessageByContent(String content) {
        return data.values().stream().filter(m -> m.getContent().contains(content)).toList();
    }

    //다건 조회 - 특정 작성자
    @Override
    public List<Message> getMessageBySender(User sender) throws RuntimeException {
        try {
            User userByUUID = userService.getUserByUUID(sender.getId().toString());
            return data.values().stream().filter(m -> m.getSender().equals(userByUUID)).toList();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    //다건 조회 - 생성 날짜
    //todo
    //날짜 포멧 오류 등 예외 처리 필요-검색 시 기준을 날짜로 할지
    @Override
    public List<Message> getMessageByCreatedAt(Long createdAt) {
        return data.values().stream().filter(m -> m.getCreatedAt().equals(createdAt)).toList();
    }

    //다건 조회 - 특정 채널
    @Override
    public List<Message> getMessagesByChannel(Channel channel) throws RuntimeException {
        try {
            Channel channelByUUID = channelService.getChannelByUUID(channel.getId().toString());
            return data.values().stream().filter(m -> m.getChannel().equals(channelByUUID)).toList();
        } catch (Exception e) {
            System.out.println("Failed to get messages. Channel: " + channel.getId() + " Message: " + e.getMessage());
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
    }

    //수정
    @Override
    public Message updateMessage(String messageId, String newContent) throws RuntimeException {

        Message message = data.get(UUID.fromString(messageId));

        if (message == null) {
            System.out.println("Failed to update message. Message not found.");
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (newContent.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        message.setContent(newContent);

        if (!message.getContent().equals(newContent)) {
            message.setUpdatedAt();
        }
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) throws RuntimeException {
        Message msg = data.get(UUID.fromString(message.getId().toString()));

        if (msg == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND, String.format("Message with id %s not found", message.getId()));
        }
        //만약 delete 한 후에도 객체가 필요하다면 반환값을 boolean 에서 Message 로 바꾸기
        return true;
    }
}
