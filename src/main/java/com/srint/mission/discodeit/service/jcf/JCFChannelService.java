package com.srint.mission.discodeit.service.jcf;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public UUID create(Channel channel) {
        data.put(channel.getId(), channel);
        return channel.getId();
    }

    @Override
    public Channel read(UUID id) {
        Channel channel = data.get(id);
        return channel;
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel updatedChannel) {
        if(data.containsKey(id)){
            updatedChannel.setUpdatedAt(System.currentTimeMillis());
            data.put(id, updatedChannel);
            return updatedChannel;
        }
        throw new NoSuchElementException("not found");
    }

    @Override
    public UUID delete(UUID id) {
        Channel channel = data.remove(id);
        return channel.getId();
    }
}
