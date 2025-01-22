package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.factory.repository.UserRepositoryFactory;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;

public class UserServiceFactory {

  private UserServiceFactory() {
  }

  public static UserService createUserService(ServiceType serviceType, StorageType storageType) {

    UserRepository userRepository = UserRepositoryFactory.createUserRepository(storageType);

    return switch (serviceType) {
      case JCF -> JCFUserService.getInstance();
      case FILE -> FileUserService.getInstance();
      case BASIC -> BasicUserService.getInstance(userRepository);
    };
  }
}
