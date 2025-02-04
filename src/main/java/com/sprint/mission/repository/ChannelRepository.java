package com.sprint.mission.repository;

import com.sprint.mission.entity.Channel;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel create(Channel channel) throws IOException;
    Channel findById(UUID id) throws IOException, ClassNotFoundException;
    Set<Channel> findAll() throws IOException;
    //Channel updateChannelName(Channel updatingChannel);

    void delete(Channel deletingChannel);
}
