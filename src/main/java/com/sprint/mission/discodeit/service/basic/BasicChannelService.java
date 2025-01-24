package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;


import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), type, channelName, description);
        channelRepository.createChannel(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return channelRepository.getChannel(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.getAllChannels();
    }

    @Override
    public void updateChannel(UUID id, String channelName) {
        Channel existingChannel = channelRepository.getChannel(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + id));
        existingChannel.update(channelName);
        channelRepository.updateChannel(id, channelName);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.getChannel(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + id));
        channelRepository.deleteChannel(id);
    }
}
