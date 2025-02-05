package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.factory.repository.BasicRepositoryFactory;
import com.sprint.mission.discodeit.factory.repository.RepositoryFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class BasicServiceFactory implements ServiceFactory {
    private static BasicServiceFactory instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;
    private final RepositoryFactory repositoryFactory;

    private BasicServiceFactory(String mode) {
        this.repositoryFactory = BasicRepositoryFactory.getInstance(mode);
        this.userService = new BasicUserService(repositoryFactory.getUserRepository());
        this.channelService = new BasicChannelService(repositoryFactory.getChannelRepository(), userService);
        this.messageService = new BasicMessageService(repositoryFactory.getMessageRepository(), userService, channelService);
    }

    public static BasicServiceFactory getInstance(String mode) {
        if (instance == null) {
            instance = new BasicServiceFactory(mode);
        }
        return instance;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public ChannelService getChannelService() {
        return channelService;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }
}
