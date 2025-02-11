package com.sprint.mission.discodeit.repository.channel.interfaces;

import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

    Channel save(Channel channel);

    Optional<Channel> findOneById(UUID uuid);

    List<Channel> findAllByUserId(UUID userId);

    void deleteById(UUID uuid);
}
