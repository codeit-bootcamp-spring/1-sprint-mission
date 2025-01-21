package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static final JCFChannelService jcfChannelService = new JCFChannelService();
    private final UserService userService;
    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>(1000);
        userService = JCFUserService.getInstance();
    }

    public static JCFChannelService getInstance() {
        return jcfChannelService;
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = Channel.from(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel입니다."));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateName(UUID channelId, String name) {
        Channel channel = Optional.ofNullable(data.get(channelId))
                        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel입니다."));
        channel.updateName(name);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel입니다."));
        channel.addUser(userService.readUser(userId));
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel입니다."));
        channel.deleteUser(userId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
