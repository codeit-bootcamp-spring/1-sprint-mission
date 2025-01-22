package com.sprint.misson.discordeit.factory.service;

import com.sprint.misson.discordeit.factory.repository.BasicRepositoryFactory;
import com.sprint.misson.discordeit.factory.repository.RepositoryFactory;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;
import com.sprint.misson.discordeit.service.basic.BasicChannelService;
import com.sprint.misson.discordeit.service.basic.BasicMessageService;
import com.sprint.misson.discordeit.service.basic.BasicUserService;

public class BasicServiceFactory implements ServiceFactory {
    private static BasicServiceFactory instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;
    private final RepositoryFactory repositoryFactory;

    private BasicServiceFactory(String mode) {
        this.repositoryFactory = BasicRepositoryFactory.getInstance(mode);
        this.userService = createUserService();
        this.channelService = createChannelService();
        this.messageService = createMessageService();
    }

    public static BasicServiceFactory getInstance(String mode) {
        if (instance == null) {
            instance = new BasicServiceFactory(mode);
        }
        return instance;
    }
    //레포지토리 어떻게 분리할 수 있을까,,,,

    @Override
    public UserService createUserService() {
        if (userService == null) {
            userService = new BasicUserService(repositoryFactory.getUserRepository());
        }
        return userService;
    }

    @Override
    public ChannelService createChannelService() {
        if (channelService == null) {
            channelService = new BasicChannelService(repositoryFactory.getChannelRepository(), userService);
        }
        return channelService;
    }

    @Override
    public MessageService createMessageService() {
        if (messageService == null) {
            messageService = new BasicMessageService(repositoryFactory.getMessageRepository(), userService, channelService);
        }
        return messageService;
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
