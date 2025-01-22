package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.util.StorageType;

public class MessageRepositoryFactory {

  private MessageRepositoryFactory() {
  }

  public static MessageRepository createMessageRepository(StorageType storageType) {
    return switch (storageType) {
      case FILE -> FileMessageRepository.getInstance();
      case JCF -> JCFMessageRepository.getInstance();
    };
  }
}
