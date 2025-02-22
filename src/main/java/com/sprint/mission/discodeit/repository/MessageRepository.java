package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
  Message save(Message message);

  Optional<Message> findById(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId); // 채널 ID가 있어야 채널에 있는 메세지를 삭제할 수 있음

  boolean existsById(UUID messageId);

  void deleteById(UUID messageId);

  void deleteAllByChannelId(UUID channelId);
}
