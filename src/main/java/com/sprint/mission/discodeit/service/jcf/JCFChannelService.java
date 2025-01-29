package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
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
    public Channel createChannel(ChannelDto channelDto) {
        Channel channel = Channel.of(channelDto.getName(), channelDto.getDescription());
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
    public void updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = readChannel(channelId);
        channel.updateName(channelDto.getName());
        channel.updateDescription(channelDto.getDescription());
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = readChannel(channelId);
        channel.addUser(userService.readUser(userId));
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = readChannel(channelId);
        channel.deleteUser(userId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
