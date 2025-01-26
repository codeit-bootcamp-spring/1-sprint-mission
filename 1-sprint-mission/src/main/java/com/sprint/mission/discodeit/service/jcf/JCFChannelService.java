package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        String channelId = UUID.randomUUID().toString();
        Channel channel = new Channel(channelId, type, name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findById(String channelId) {
        return channelRepository.findById(channelId);
    }
}
