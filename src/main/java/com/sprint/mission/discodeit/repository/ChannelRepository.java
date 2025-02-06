package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> getChannelById(UUID id);
    List<Channel> getAllChannels();
    boolean exitsById(UUID id);
    void deleteChannel(UUID id);
}
