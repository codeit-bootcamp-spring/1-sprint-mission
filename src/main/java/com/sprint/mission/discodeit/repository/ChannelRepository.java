package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    Channel save(Channel channel);
    void deleteById(UUID id);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    List<Channel> findByType(ChannelType type);
    List<Channel> findByNameContaining(String name);
}