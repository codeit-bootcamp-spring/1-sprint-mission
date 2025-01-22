package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.factory.repository.ChannelRepositoryFactory;
import com.sprint.mission.discodeit.factory.repository.MessageRepositoryFactory;
import com.sprint.mission.discodeit.factory.repository.UserRepositoryFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageServiceV2;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;

public class MessageServiceFactory {

  private MessageServiceFactory() {
  }

  public static MessageServiceV2<ChatChannel> createMessageService(
      ServiceType serviceType,
      ServiceType userServiceType,
      StorageType messageStorageType,
      StorageType userStorageType,
      StorageType channelStorageType
  ) {

    MessageRepository messageRepository = MessageRepositoryFactory.createMessageRepository(messageStorageType);

    UserRepository userRepository = UserRepositoryFactory.createUserRepository(userStorageType);

    ChannelRepository channelRepository = ChannelRepositoryFactory.createChannelRepository(channelStorageType);

    UserService userService = UserServiceFactory.createUserService(userServiceType, userStorageType);

    return switch (serviceType) {
      case JCF -> JCFMessageServiceV2.getInstance(userService);
      case FILE -> FileMessageService.getInstance(userService);
      case BASIC -> BasicMessageService.getInstance(messageRepository, userRepository, channelRepository);
    };
  }
}
