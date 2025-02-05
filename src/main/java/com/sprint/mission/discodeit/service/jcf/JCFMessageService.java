package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class JCFMessageService implements MessageService {

    private final HashMap<String, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    //생성
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
            data.put(message.getId(), message);
            return message;
        } catch (CustomException e) {
            System.out.println("Failed to create message. User: " + userId + " Channel: " + channelId + " Content: " + content);
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
        return data.values().stream().toList();
    }

    //단일 조회 - uuid
    @Override
    public Message getMessageByUUID(String messageId) throws CustomException {
        Message message = data.get(messageId);
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
    public List<Message> getMessageBySenderId(String senderId) throws CustomException {
        try {
            return data.values().stream().filter(m -> m.getSenderId().equals(senderId)).toList();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    //다건 조회 - 생성 날짜
    //todo
    //날짜 포멧 오류 등 예외 처리 필요-검색 시 기준을 날짜로 할지
    @Override
    public List<Message> getMessageByCreatedAt(Instant createdAt) {
        return data.values().stream().filter(m -> m.getCreatedAt().equals(createdAt)).toList();
    }

    //다건 조회 - 특정 채널
    @Override
    public List<Message> getMessagesByChannel(Channel channel) throws CustomException {
        try {
            Channel channelByUUID = channelService.getChannelByUUID(channel.getId());
            return data.values().stream().filter(m -> m.getChannelId().equals(channelByUUID.getId())).toList();
        } catch (Exception e) {
            System.out.println("Failed to get messages. Channel: " + channel.getId() + " Message: " + e.getMessage());
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
    }

    //수정
    @Override
    public Message updateMessage(String messageId, String newContent, Instant updatedAt) throws CustomException {

        Message message = data.get(messageId);

        if (message == null) {
            System.out.println("Failed to update message. Message not found.");
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (newContent.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        message.setContent(newContent);

        if (!message.getContent().equals(newContent)) {
            message.setUpdatedAt(updatedAt);
        }
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) throws CustomException {
        return data.remove(message.getId()) != null;
    }
}
