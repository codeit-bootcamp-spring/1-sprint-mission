package com.spirnt.mission.discodeit.jcf;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService(){
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel updatedChannel) {
        if(data.containsKey(id)){
            Channel existingChannel = data.get(id);
            existingChannel.updateChannelName(updatedChannel.getChannelName());
            existingChannel.updateDescription(updatedChannel.getDescription());
            return existingChannel;
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
