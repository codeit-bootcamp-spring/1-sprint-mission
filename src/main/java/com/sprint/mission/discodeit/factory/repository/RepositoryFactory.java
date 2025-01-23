package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.util.StorageType;

public class RepositoryFactory {
  private RepositoryFactory(){}
  public static UserRepository createUserRepository(StorageType type) {
    return switch (type) {
      case FILE -> FileUserRepository.getInstance();
      case JCF -> JCFUserRepository.getInstance();
    };
  }

  public static MessageRepository createMessageRepository(StorageType storageType) {
    return switch (storageType) {
      case FILE -> FileMessageRepository.getInstance();
      case JCF -> JCFMessageRepository.getInstance();
    };
  }

  public static ChannelRepository createChannelRepository(StorageType type) {
    return switch (type) {
      case JCF -> JCFChannelRepository.getInstance();
      case FILE -> FileChannelRepository.getInstance();
    };
  }
}
