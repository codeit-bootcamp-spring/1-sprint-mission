package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.util.StorageType;

public class ChannelRepositoryFactory{

  private ChannelRepositoryFactory(){}
  public static ChannelRepository createChannelRepository(StorageType type) {
    return switch (type) {
      case JCF -> JCFChannelRepository.getInstance();
      case FILE -> FileChannelRepository.getInstance();
    };
  }
}
