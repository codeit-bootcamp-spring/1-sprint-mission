package com.sprint.mission.discodeit.repository.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelInMemoryRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelUUIDStore = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        Channel savedChannel = channelUUIDStore.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findOneById(UUID channelId) {
        return Optional.ofNullable(channelUUIDStore.get(channelId));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void deleteById(UUID uuid) {
        channelUUIDStore.remove(uuid);
    }

    @Override
    public boolean isExistUser(User user, Channel channel) {
        Set<UUID> participantUserId = channelUUIDStore.get(channel.getId()).getParticipantUserId();
        return participantUserId.contains(user.getId());
    }
}
