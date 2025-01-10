package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_channel implements ChannelService {
    private final List<Channel> channelSet;

    public JCF_channel() {
        this.channelSet = new ArrayList<>();
    }
    @Override
    public void Creat(Channel channel) {
        channelSet.add(channel);
    }

    @Override
    public void Delete(Channel channel) {
        channelSet.remove(channel);

    }

    @Override
    public void Update(Channel channel, Channel updateChannel) {
        channelSet.replaceAll(channels -> channels.equals(channel) ? updateChannel : channels);

    }

    @Override
    public List<Channel> Write(UUID id) {
        return channelSet.stream().filter(channel_id -> channel_id.GetId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Channel> AllWrite() {
        return channelSet;

    }
}
