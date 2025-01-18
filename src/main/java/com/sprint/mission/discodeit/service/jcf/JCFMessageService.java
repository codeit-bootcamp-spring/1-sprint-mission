package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void addMessage(Message message) {
        // 유효한 사용자와 채널 ID인지 확인
        if (!userService.getAllUsers().stream()
                .anyMatch(user -> user.getId().equals(message.getUserId()))) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID: " + message.getUserId());
        }

        if (!channelService.getAllChannels().stream()
                .anyMatch(channel -> channel.getId().equals(message.getChannelId()))) {
            throw new IllegalArgumentException("유효하지 않은 채널 ID: " + message.getChannelId());
        }

        data.put(message.getId(), message);
    }

    @Override
    public Message getMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = data.get(id);
        if (message == null) {
            throw new NoSuchElementException("존재하지 않는 메시지 ID: " + id);
        }

        message.updateContent(newContent);
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
