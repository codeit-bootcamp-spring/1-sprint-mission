package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> messages = new ArrayList<>();
    private final ChannelService channelService;

    public JCFMessageService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(String username, UUID channelId, String content) {
        // 메시지 생성
        Message message = new Message(username, channelId, content);

        // 메시지 저장
        messages.add(message);

        // 채널에 메시지 추가
        Channel channel = channelService.getChannelById(channelId);
        if (channel != null) {
            channel.addMessage(message);
        }

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
