package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData = new HashMap<>();
    private final UserService userService; // UserService 의존성
    private final ChannelService channelService; // ChannelService 의존성

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }
    @Override
    public void create(Message message) {
        if (userService.read(message.getSender().getId()) == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }
        if (channelService.read(message.getChannel().getId()) == null) {
                throw new IllegalArgumentException("유효하지 않은 채널입니다.");
        }
        messageData.put(message.getSender().getId(), message); // 메시지 저장
    }

    @Override
    public Message read(UUID id) {
        return messageData.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public void update(UUID id, Message message) {
        if(messageData.containsKey(id)){
            message.setUpdatedAt(System.currentTimeMillis());
            messageData.put(id,message);
        }
    }

    @Override
    public void delete(UUID id) {
        messageData.remove(id);
    }
}
