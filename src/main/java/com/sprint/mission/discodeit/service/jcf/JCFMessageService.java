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
    public void create(Message message) {
        // 발신자와 채널이 존재하는지 검증
        if (userService.read(message.getSenderId()).isEmpty()) {
            throw new IllegalArgumentException("User not found: " + message.getSenderId());
        }
        if (channelService.read(message.getChannelId()).isEmpty()) {
            throw new IllegalArgumentException("Channel not found: " + message.getChannelId());
        }

        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message);
        } else {
            throw new IllegalArgumentException("Message not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
