package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import java.util.List;
import java.util.UUID;

public interface UserRepository {

  void creat(User user);

  void delete(UUID userId);

  //여기아

  void update(UUID userId, String name);

  UUID findByName(String name);

  List<User> findByAll();
}
