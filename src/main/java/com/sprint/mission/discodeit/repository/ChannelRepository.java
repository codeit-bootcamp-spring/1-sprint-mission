package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BaseChannel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {
  BaseChannel save(BaseChannel baseChannel);
  Optional<BaseChannel> findById(String id);
  List<BaseChannel> findAll();
  BaseChannel update(BaseChannel channel);
  void delete(String id);
  void clear();
}
