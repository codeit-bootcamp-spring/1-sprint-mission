package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static final JCFChannelService jcfChannelService = new JCFChannelService();
    private final UserService userService;
    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>();
        userService = JCFUserService.getInstance();
    }

    public static JCFChannelService getInstance() {
        return jcfChannelService;
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel readChannel(UUID channelId) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new RuntimeException("등록되지 않은 channel입니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateName(UUID channelId, String name) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new RuntimeException("등록되지 않은 channel입니다.");
        }
        channel.updateName(name);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new RuntimeException("등록되지 않은 channel입니다.");
        }
        channel.addUser(userService.readUser(userId));
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new RuntimeException("등록되지 않은 channel입니다.");
        }
        channel.deleteUser(userId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
