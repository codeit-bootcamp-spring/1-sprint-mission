package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  Channel save(Channel channel);

  Optional<Channel> findById(UUID id);

  List<Channel> findAll();

  boolean existsId(UUID id);

  boolean existsName(String channelName);

  void delete(UUID id);
}
