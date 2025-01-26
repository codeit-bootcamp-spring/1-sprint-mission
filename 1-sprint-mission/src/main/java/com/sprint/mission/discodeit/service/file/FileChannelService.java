package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(com.sprint.mission.discodeit.model.ChannelType type, String name, String description) {
        String channelId = UUID.randomUUID().toString();
        Channel channel = new Channel(channelId, type, name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findById(String channelId) {
        return channelRepository.findById(channelId);
    }
}
