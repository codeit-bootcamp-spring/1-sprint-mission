package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        validateChannel(channel);
        channelRepository.createChannel(channel);
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        return Optional.ofNullable(channelRepository.getChannelById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not fount")));

    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.getAllChannels();
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        validateChannel(updatedChannel);
        channelRepository.updateChannel(id,updatedChannel);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.deleteChannel(id);
    }

    private void validateChannel(Channel channel) {
        if (channel.getChannel() == null || channel.getChannel().isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be empty");
        }
        if (channel.getChannel().length() > 100) {
            throw new IllegalArgumentException("Channel name cannot exceed 100 characters");
        }
        if (channel.getDescription() != null && channel.getDescription().length() > 255) {
            throw new IllegalArgumentException("Channel description cannot exceed 255 characters");
        }
    }
}
