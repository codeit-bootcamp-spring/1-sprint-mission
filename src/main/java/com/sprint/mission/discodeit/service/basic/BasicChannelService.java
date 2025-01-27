package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.ChannelType;

import java.util.List;
import java.util.Optional;

public class BasicChannelService implements ChannelService  {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) {
        channelRepository.findByName(name).ifPresent(channel -> {
            throw new ServiceException(ErrorCode.DUPLICATE_CHANNEL);});
        Channel channel = new Channel(name, description, channelType);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Optional<Channel> getChannelByName(String name) {
        return channelRepository.findByName(name);
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.findAll();
    }

    @Override
    public Channel updateType(Channel channel, ChannelType channelType) {
        existChannel(channel);
        channel.changeType(channelType);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel updateDescription(Channel channel, String description) {
        existChannel(channel);
        channel.changeDescription(description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(Channel removeChannel) {
        existChannel(removeChannel);
        channelRepository.delete(removeChannel);
    }

    private void existChannel(Channel channel) {
        channelRepository.findByName(channel.getName())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}
