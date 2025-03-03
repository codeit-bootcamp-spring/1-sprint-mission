package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id) ;
    List<Channel> findAll();
    //Channel updateChannelName(Channel updatingChannel);

    void delete(UUID channelId);

    boolean existsById(UUID id);
}
