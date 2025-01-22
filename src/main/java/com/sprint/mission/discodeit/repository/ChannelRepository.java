package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.*;

public interface ChannelRepository {
    void save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    void delete(UUID id);
}