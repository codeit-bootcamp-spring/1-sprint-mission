package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel newChannel);

    Channel getChannelById(UUID uuid);

    void deleteChannelById(UUID uuid);

    List<Channel> getAllChannels();

    void save();
}
