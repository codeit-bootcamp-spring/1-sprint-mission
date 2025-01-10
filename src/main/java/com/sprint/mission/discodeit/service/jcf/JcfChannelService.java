package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JcfChannelService implements ChannelService {

    private List<Channel> data=new ArrayList<>();
    @Override
    public void createChannel(Channel channel) {
        data.add(channel);
    }

    @Override
    public Optional<Channel> channelList(UUID id) {
        for (Channel channel : data) {
            if(channel.getId().equals(id)){
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> allChannelList() {
        return data;
    }

    @Override
    public void updateChannel(Channel updateChannel) {
        for (Channel channel : data) {
            if(channel.getId().equals(updateChannel.getId())){
                channel.updateChannelName(updateChannel.getChannelName());
                channel.updateDescription(updateChannel.getDescription());
            }
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        for (Channel channel : data) {
            if(channel.getId().equals(id)){
                data.remove(channel);
            }
        }
    }
}
