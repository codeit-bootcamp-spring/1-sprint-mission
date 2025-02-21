package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends BaseRepository<Channel, String>{
  Channel save(Channel channel);
  Optional<Channel> findById(String id);
  List<Channel> findAll();
  Channel update(Channel channel);
  void delete(String id);
  void clear();
}
