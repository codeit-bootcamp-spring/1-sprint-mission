package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelRepository {

  Channel save(Channel channel);

  Channel findByName(String name);

  Channel findById(UUID id); // findByChannelId에서 변경

  List<Channel> findAll();

  boolean existsById(UUID id);

  void deleteById(UUID id);
}