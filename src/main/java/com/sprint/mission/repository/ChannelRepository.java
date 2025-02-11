package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Channel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel) throws IOException;
    Optional<Channel> findById(UUID id) throws IOException, ClassNotFoundException;
    List<Channel> findAll() throws IOException;
    //Channel updateChannelName(Channel updatingChannel);

    void delete(Channel deletingChannel);

    boolean existsById(UUID id);
}
