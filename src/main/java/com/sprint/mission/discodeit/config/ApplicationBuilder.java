package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.factory.service.ChannelServiceFactory;
import com.sprint.mission.discodeit.factory.service.MessageServiceFactory;
import com.sprint.mission.discodeit.factory.service.UserServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;

public class ApplicationBuilder {
  private ApplicationConfig ac = new ApplicationConfig.ApplicationConfigBuilder()
      .channelServiceType(ServiceType.BASIC)
      .channelStorageType(StorageType.JCF)
      .userServiceType(ServiceType.BASIC)
      .userStorageType(StorageType.JCF)
      .messageServiceType(ServiceType.BASIC)
      .messageStorageType(StorageType.JCF)
      .build();

  public ChannelService getChannelService() {
    return ChannelServiceFactory.createChannelService(ac.getChannelServiceType(), ac.getChannelStorageType());
  }

  public UserService getUserService() {
    return UserServiceFactory.createUserService(ac.getUserServiceType(), ac.getUserStorageType());
  }
  public MessageServiceV2<ChatChannel> getMessageService() {
    return MessageServiceFactory.createMessageService(
        ac.getMessageServiceType(),
        ac.getUserServiceType(),
        ac.getMessageStorageType(),
        ac.getUserStorageType(),
        ac.getChannelStorageType()
    );
  }
}
