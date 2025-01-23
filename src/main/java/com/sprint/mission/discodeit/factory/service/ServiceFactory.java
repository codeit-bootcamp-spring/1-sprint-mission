package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.config.ApplicationConfig;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.factory.repository.RepositoryFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageServiceV2;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

  private ServiceFactory() {
  }

  public static UserService createUserService(ApplicationConfig ac) {
    return switch (ac.getUserServiceType()) {
      case JCF -> JCFUserService.getInstance();
      case FILE -> FileUserService.getInstance();
      case BASIC -> BasicUserService.getInstance(RepositoryFactory.createUserRepository(ac.getUserStorageType()));
    };
  }

  public static ChannelService createChannelService(ApplicationConfig ac) {
    return switch (ac.getChannelServiceType()) {
      case JCF -> JCFChannelService.getInstance();
      case FILE -> FileChannelService.getInstance();
      case BASIC ->
          BasicChannelService.getInstance(RepositoryFactory.createChannelRepository(ac.getChannelStorageType()));
    };
  }

  public static MessageServiceV2<ChatChannel> createMessageService(ApplicationConfig ac, UserService userService, ChannelService channelService) {
    return switch (ac.getMessageServiceType()) {
      case JCF -> JCFMessageServiceV2.getInstance(userService, channelService);
      case FILE -> FileMessageService.getInstance(userService, channelService);
      case BASIC -> BasicMessageService.getInstance(
          RepositoryFactory.createMessageRepository(ac.getMessageStorageType()),
          RepositoryFactory.createUserRepository(ac.getUserStorageType()),
          RepositoryFactory.createChannelRepository(ac.getChannelStorageType())
      );
    };
  }
}
