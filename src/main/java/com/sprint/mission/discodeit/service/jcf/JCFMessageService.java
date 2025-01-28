package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> messages = new ArrayList<>();
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message createMessage(String username, UUID channelId, String content) {
        Channel channel = channelService.getChannelById(channelId);

        //채널 확인
        if (channel == null) {
            System.out.println("Channel을 찾을 수 없습니다!");
            return null;
        }

        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("유저를 찾을 수 없습니다");
            return null;
        }

        if (!channel.isUserInChannel(user.getId())) {
            System.out.println("해당 유저는 채널에 존재하지 않습니다!");
            return null;
        }
        // 메시지 생성
        //+
        Message message = new Message(user.getUsername(), channelId, content);

        // 메시지 저장
        messages.add(message);
        channel.addMessage(message);

        System.out.println("메시지 생성 성공!");
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }


    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        Channel channel = channelService.getChannelById(channelId);
        if (channel != null) {
            return channel.getMessages();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }

    private User findUserByUsername(String username) {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    // ======================================================================================================
    public void printSingleMessage(UUID messageId) {
        Message message = getMessageById(messageId); // 단일 메시지 조회
        if (message != null) {
            Channel channel = channelService.getChannelById(message.getChannelId()); // 채널 조회
            System.out.println("User: " + message.getUsername() +
                    ", Message Content: " + message.getContent() +
                    ", Channel Name: " + (channel != null ? channel.getChannelName() : "Unknown") +
                    ", Channel UUID: " + message.getChannelId() +
                    ", Message UUID: " + message.getId() +
                    ", Created At: " + message.getCreatedAt() +
                    ", Updated At: " + message.getUpdatedAt());
        } else {
            System.out.println("해당 ID의 메시지를 찾을 수 없습니다.");
        }
    }

    public void printAllMessages() {
        List<Message> messages = getAllMessages(); // 전체 메시지 조회
        if (!messages.isEmpty()) {
            for (Message message : messages) {
                Channel channel = channelService.getChannelById(message.getChannelId()); // 채널 조회
                System.out.println("User: " + message.getUsername() +
                        ", Message Content: " + message.getContent() +
                        ", Channel Name: " + (channel != null ? channel.getChannelName() : "Unknown") +
                        ", Channel UUID: " + message.getChannelId() +
                        ", Message UUID: " + message.getId() +
                        ", Created At: " + message.getCreatedAt() +
                        ", Updated At: " + message.getUpdatedAt());
            }
        } else {
            System.out.println("현재 등록된 메시지가 없습니다.");
        }
    }
    // ======================================================================================================


    @Override
    public Message updateMessageContent(UUID messageId, String newContent) {
        Message message = getMessageById(messageId);
        if (message != null) {
            message.updateContent(newContent);
        }
        return message;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {//삭제의 경우 읽기 전용인 향상된 for문을 사용하면 오류 = 일반 for문을 사용하였다.
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(messageId)) {
                messages.remove(i);
                return true;
            }
        }
        return false;
    }



}
