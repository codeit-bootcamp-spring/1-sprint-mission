package com.sprint.mission.discodeit.service.file.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.file.FileUser;
import java.util.List;
import java.util.UUID;

public interface FileMessageRepository {

  void creat(UUID userId, String content, UUID channelId, FileUser fileUser);

  void delete(UUID messageId);

  void update(UUID messageId, String content);

  List<String> getMessageList(List<UUID> messageIdList, FileUser fileUser);

  List<Message> findByAll();
}
