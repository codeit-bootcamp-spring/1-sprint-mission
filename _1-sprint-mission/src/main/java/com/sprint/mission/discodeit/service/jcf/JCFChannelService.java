package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public void create(Channel channel) {
        channelData.put(channel.getId(),channel);
    }

    @Override
    public Channel read(UUID id) {
        return channelData.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public void update(UUID id, Channel channel) {
        if(channelData.containsKey(id)){
            channel.setUpdatedAt(System.currentTimeMillis());
            channelData.put(id,channel);
        }
    }
    @Override
    public void delete(UUID id){
        channelData.remove(id);
    }
}
