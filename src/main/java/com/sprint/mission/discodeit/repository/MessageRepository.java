package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageRepository {
  Message create(Message message);
  Optional<Message> findById(String id);
  List<Message> findByChannel(String channelId);
  List<Message> findAll();
  Message findLatestChannelMessage(String channelId);
  Message update(Message message);
  void delete(String id);
  void deleteByChannel(String channelId);
  void clear();
}
