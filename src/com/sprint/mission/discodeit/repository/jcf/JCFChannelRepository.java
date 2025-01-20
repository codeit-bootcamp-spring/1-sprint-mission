package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        data = new HashMap<>();
    }
    /**
     * Create the Channel while ignoring the {@code createAt} and {@code updateAt} fields from {@code channelInfoToCreate}
     */
    @Override
    public Channel createChannel(Channel channelInfoToCreate) {
        Channel channelToCreate = Channel.createChannel(
                channelInfoToCreate.getId(),
                channelInfoToCreate.getName()
        );

        return Optional.ofNullable(data.putIfAbsent(channelToCreate.getId(), channelToCreate))
                .map(existingChannel -> Channel.createEmptyChannel())
                .orElse(channelToCreate);
    }

    @Override
    public Channel findChannelById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Channel.createEmptyChannel());
    }

    /**
     * Update the Channel while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code channelInfoToUpdate}
     */
    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) {
        Channel existingChannel = findChannelById(key);
        Channel channelToUpdate = Channel.createChannel(
                key,
                existingChannel.getCreateAt(),
                channelInfoToUpdate.getName()
        );

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, channel) -> channelToUpdate))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Channel.createEmptyChannel());
    }
}
