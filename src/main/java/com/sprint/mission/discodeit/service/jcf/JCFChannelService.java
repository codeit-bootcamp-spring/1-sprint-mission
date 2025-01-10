package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public void addChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel getChannel(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String newName) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.updateName(newName);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
