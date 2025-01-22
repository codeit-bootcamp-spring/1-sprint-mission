package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;

public class UserRepositoryFactory {

  private UserRepositoryFactory() {
  }

  public static UserRepository createUserRepository(StorageType type) {
    return switch (type) {
      case FILE -> FileUserRepository.getInstance();
      case JCF -> JCFUserRepository.getInstance();
    };
  }
}
