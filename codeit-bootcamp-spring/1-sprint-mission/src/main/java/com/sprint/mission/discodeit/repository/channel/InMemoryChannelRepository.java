package com.sprint.mission.discodeit.repository.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> uuidChannels = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        Channel savedChannel = uuidChannels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findOneById(UUID uuid) {
        return Optional.ofNullable(uuidChannels.get(uuid));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void deleteById(UUID uuid) {
        uuidChannels.remove(uuid);
    }
}
