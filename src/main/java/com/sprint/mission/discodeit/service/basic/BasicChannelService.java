package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        channelRepository.createChannel(channel);
    }

    @Override
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), channelName, description);
        channelRepository.createChannel(channel);  // Repository에 저장
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
        Channel channel = getChannel(id);
        channel.update(channelName);  // 비즈니스 로직
        channelRepository.updateChannel(id, channelName);  // Repository에 반영
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.deleteChannel(id);
    }
}
