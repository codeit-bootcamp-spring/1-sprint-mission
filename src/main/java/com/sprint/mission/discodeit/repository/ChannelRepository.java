package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByType(Channel.ChannelType type);

  List<Channel> findByIdInOrType(List<UUID> channelIds, Channel.ChannelType type);


}
