package com.sprint.mission.discodeit.repository.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    @Override
    public Channel save(Channel channel) {
        return null;
    }

    @Override
    public Optional<Channel> findOneById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
