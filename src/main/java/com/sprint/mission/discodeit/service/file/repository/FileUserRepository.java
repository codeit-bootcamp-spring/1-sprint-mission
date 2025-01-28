package com.sprint.mission.discodeit.service.file.repository;

import com.sprint.mission.discodeit.entity.User;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileUserRepository {

  void creat(String name);

  void delete(UUID userId);

  void update(UUID userId, String name);

  UUID findByName(String name);

  List<User> findByAll() throws IOException;
}
