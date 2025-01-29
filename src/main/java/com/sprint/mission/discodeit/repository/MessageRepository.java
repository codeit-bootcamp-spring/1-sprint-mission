package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCF_user;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {

  void creat(Message message);

  void delete(UUID messageId);

  void update(UUID messageId, String updateMessage);

  Message findById(UUID messageId);

  List<Message> findByAll();
}
