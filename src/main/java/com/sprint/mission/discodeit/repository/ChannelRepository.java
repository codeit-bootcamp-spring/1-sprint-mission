package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    void deleteById(UUID id);
    List<Channel> findAllPrivateChannelsByUserId(UUID userId);
    List<Channel> findAllChannelsForUser(UUID userId);

    // 🔥 existsById() 추가
    boolean existsById(UUID id);
}
