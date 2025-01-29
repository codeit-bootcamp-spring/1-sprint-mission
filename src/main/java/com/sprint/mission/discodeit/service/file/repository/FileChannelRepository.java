package com.sprint.mission.discodeit.service.file.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface FileChannelRepository {

  void creat(String name);

  void delete(UUID channelId);

  void update(UUID channelId, String title);

  UUID findByTitle(String title);

  List<Channel> findByAll();

  void addMessage(UUID messageId, UUID channelId);
}
