package com.example.mission.discodeit.repository;

import com.example.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByUserId(UUID userId);
}

