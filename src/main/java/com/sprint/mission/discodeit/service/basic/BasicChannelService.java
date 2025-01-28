package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.ChannelUtil.checkValid;
import static com.sprint.mission.discodeit.util.ChannelUtil.validChannelId;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) {
        checkValid(name, description, channelType);

        Channel channel = new Channel(name, description, channelType);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        validChannelId(channel);
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String name, String description, ChannelType channelType) {
        Channel checkChannel = channelRepository.findById(channelId);

        validChannelId(checkChannel);
        checkValid(name, description, channelType);

        checkChannel.update(name, description, channelType);
        return channelRepository.save(checkChannel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel checkChannel = channelRepository.findById(channelId);

        validChannelId(checkChannel);
        channelRepository.delete(channelId);
    }
}
