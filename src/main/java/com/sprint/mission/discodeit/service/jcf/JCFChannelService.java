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
    public Channel createChannel(String name, String description){
        Channel channel = new Channel(name, description);
        data.put(channel.getId(), channel);
        return channel;
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
    public Channel updateChannel(UUID id, String name, String description) {
        Channel channel = data.get(id);
        if(channel != null) {
            channel.update(name, description);
            return channel;

        }
        return null;
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
