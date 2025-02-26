package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface ChannelRepository {
    Channel save(Channel channel);

    Channel findByChannelname (String channelname);

    Channel findById(UUID id);

    List<Channel> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);
    
}