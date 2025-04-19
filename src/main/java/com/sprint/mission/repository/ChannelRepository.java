package com.sprint.mission.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID>, CustomChannelRepository {

    List<Channel> findAllByIdIn(List<UUID> ids);
    List<Channel> findAllByChannelType(ChannelType channelType);
}