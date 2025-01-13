package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.UUID;

public class ChannelServiceProxy implements ChannelService {
    private final ChannelService channelService;

    public ChannelServiceProxy(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Channel createChannel(Channel channelToCreate) {
        return channelService.createChannel(channelToCreate);
    }

    @Override
    public Channel findChannelById(UUID key) {
        return channelService.findChannelById(key);
    }

    @Override
    public Channel updateChannelById(UUID key, Channel channelToUpdate) {
        return channelService.updateChannelById(key, channelToUpdate);
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return channelService.deleteChannelById(key);
    }
}
