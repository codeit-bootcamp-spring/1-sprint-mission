package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  Channel save(Channel channel);

  Optional<Channel> findById(UUID channelId);

  List<Channel> findAll();

  boolean existsById(UUID channelId);

  void deleteById(UUID channelId);
}
