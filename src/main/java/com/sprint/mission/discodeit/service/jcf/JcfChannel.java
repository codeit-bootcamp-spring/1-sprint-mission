package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JcfChannel implements ChannelService {
    private final List<Channel> data = new ArrayList<>();

    @Override
    public void create(Channel channel) {
        data.add(channel);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void update(UUID id, Channel updatedChannel) {
        for (Channel channel : data) {
            if(channel.getId().equals(id)) {
                channel.updateName(updatedChannel.getName());
                return;
            }
        }
    }

    @Override
    public void delete(UUID id) {
        for (Channel channel : data) {
            if(channel.getId().equals(id)){
                data.remove(channel);
            }
        }
    }
}
