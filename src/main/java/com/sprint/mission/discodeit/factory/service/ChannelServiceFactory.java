package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.factory.repository.ChannelRepositoryFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;

public class ChannelServiceFactory {

  private ChannelServiceFactory(){}

  public static ChannelService createChannelService(ServiceType serviceType, StorageType storageType) {

    ChannelRepository repository = ChannelRepositoryFactory.createChannelRepository(storageType);

    return switch (serviceType) {
      case JCF -> JCFChannelService.getInstance();
      case FILE -> FileChannelService.getInstance();
      case BASIC -> BasicChannelService.getInstance(repository);
    };
  }
}
