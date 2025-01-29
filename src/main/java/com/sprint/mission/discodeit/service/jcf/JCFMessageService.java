package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void createMessage(Message message) {
        if (userService.getUser(message.getUser().getId()) == null) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        if (channelService.getChannel(message.getChannel().getId()) == null) {
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        }

        data.put(message.getId(), message);
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        // 유저와 채널이 존재하는지 확인
        User user = userService.getUser(authorId);
        if (user == null) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        Channel channel = channelService.getChannel(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        }

        // 메시지 생성
        UUID messageId = UUID.randomUUID();
        long currentTime = System.currentTimeMillis();
        Message message = new Message(messageId, currentTime, currentTime, content, user, channel, authorId, channelId);

        // 메시지 저장
        data.put(messageId, message);

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
    public void updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
