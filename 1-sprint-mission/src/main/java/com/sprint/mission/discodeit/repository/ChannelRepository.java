package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {
    void save(Channel channel);
    Optional<Channel> findByUuid(String channelUuid);
    List<Channel>findAll();
    void delete(String channelUuid);
}
