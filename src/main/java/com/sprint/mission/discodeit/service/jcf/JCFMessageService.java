package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;


public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final JCFUserService userService;
    private final JCFChannelService channelService;

    public JCFMessageService(JCFUserService userService, JCFChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    private void validateUser(User user, UUID userId) {
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId: " + userId);
        }
    }

    private void validateChannel(Channel channel, UUID channelId) {
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다. channelId: " + channelId);
        }
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        User user = userService.getUser(userId);
        Channel channel = channelService.getChannel(channelId);

        validateUser(user, userId);
        validateChannel(channel, channelId);

        Message message = new Message(userId, channelId, content);
        data.put(message.getId(), message);
        return message;
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
    public Message updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
            return message;
        }
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
