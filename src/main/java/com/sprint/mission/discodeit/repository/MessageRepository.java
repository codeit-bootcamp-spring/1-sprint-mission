package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends BaseRepository<Message,String> {
  Message create(Message message);

  List<Message> findByChannel(String channelId);
  List<Message> findAll();
  Optional<Message> findLatestChannelMessage(String channelId);
  Message update(Message message);
  void delete(String id);
  void deleteByChannel(String channelId);
  void clear();
}
