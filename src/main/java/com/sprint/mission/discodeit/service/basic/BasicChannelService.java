package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        return channelRepository.save(new Channel(channelName));
    }

    @Override
    public Map<UUID, Channel> getChannels() {
        return channelRepository.getAllChannels().stream()
                .collect(Collectors.toMap(Channel::getId, channel -> channel));
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid));
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID) {

        return Optional.ofNullable(channelRepository.getChannelById(channelUUID))
                .map(channel -> {
                    channel.addMessageToChannel(messageUUID);
                    return channelRepository.save(channel);
                });
    }

    @Override
    public Optional<Channel> updateChannel(UUID uuid, String channelName) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid))
                .map(channel -> {
                    channel.updateChannelName(channelName);
                    return channelRepository.save(channel);
                });
    }

    @Override
    public Optional<Channel> deleteChannel(UUID uuid) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid))
                .map(channel -> {
                    channelRepository.deleteChannelById(uuid);
                    channelRepository.save();
                    return channel;
                });
    }
}
