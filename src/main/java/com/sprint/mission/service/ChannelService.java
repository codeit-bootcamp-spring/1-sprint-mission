package com.sprint.mission.service;


import com.sprint.mission.entity.Channel;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {

    Channel createOrUpdate(Channel channel) throws IOException;
    Set<Channel> findAll();
    Channel findById(UUID id);
    Channel update(Channel channel, String newName);
    void delete(Channel channel) throws IOException;
    void validateDuplicateName(String name);
}