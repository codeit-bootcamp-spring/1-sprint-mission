package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
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
        if(userService.getUser(message.getUser().getId()) == null) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        if(channelService.getChannel(message.getChannel().getId()) == null) {
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
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
