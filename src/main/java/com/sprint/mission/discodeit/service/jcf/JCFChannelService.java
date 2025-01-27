package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.ChannelType;


import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelRepository;

    public JCFChannelService() {
        this.channelRepository = new HashMap<>();
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) throws IllegalArgumentException {
        if (channelRepository.values().stream()
                .anyMatch(user -> user.getName().equals(name))) {
            throw new ServiceException(ErrorCode.DUPLICATE_CHANNEL);
        }

        Channel createChannel = new Channel(name, description, channelType);
        channelRepository.put(createChannel.getId(), createChannel);
        return createChannel;
    }

    @Override
    public Optional<Channel> getChannelByName(String name) {
        for (Channel channel : channelRepository.values()) {
            if (channel.getName().equals(name)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Channel updateType(Channel channel, ChannelType channelType) {
        validateChannel(channel);
        channel.changeType(channelType);
        return channel;
    }

    @Override
    public Channel updateDescription(Channel channel, String description) {
        validateChannel(channel);
        channel.changeDescription(description);
        return channel;
    }

    @Override
    public void delete(Channel channel) {
        validateChannel(channel);
        channelRepository.remove(channel.getId());
    }

    private void validateChannel(Channel channel) {
        Channel findChannel = getChannelByName(channel.getName())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}