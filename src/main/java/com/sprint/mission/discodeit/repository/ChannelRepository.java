package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
  boolean save(ChannelDto channelDto);

  Optional<Channel> findById(UUID id);

  List<ChannelDto> findAll();

  boolean updateOne(UUID id, String name, String topic);

  boolean deleteOne(UUID id);
}
