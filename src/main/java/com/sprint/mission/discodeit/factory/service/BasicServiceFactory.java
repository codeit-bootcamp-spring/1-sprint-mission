package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.factory.repository.BasicRepositoryFactory;
import com.sprint.mission.discodeit.factory.repository.RepositoryFactory;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

public class BasicServiceFactory implements ServiceFactory {
    private static BasicServiceFactory instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;
    private UserStatusService userStatusService;
    private ReadStatusService readStatusService;
    private BinaryContentService binaryContentService;
    private final RepositoryFactory repositoryFactory;

    private BasicServiceFactory(String mode) {
        this.repositoryFactory = BasicRepositoryFactory.getInstance(mode);
        this.userStatusService = new BasicUserStatusService(repositoryFactory.getUserStatusRepository());
        this.readStatusService = new BasicReadStatusService(repositoryFactory.getReadStatusRepository());
        this.binaryContentService = new BasicBinaryContentService(repositoryFactory.getBinaryContentRepository());
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

    @Override
    public UserStatusService getUserStatusService() {
        return userStatusService;
    }

    @Override
    public ReadStatusService getReadStatusService() {
        return readStatusService;
    }

    @Override
    public BinaryContentService getBinaryContentService() {
        return binaryContentService;
    }

}
