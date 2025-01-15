package com.sprint.mission.discodeit.config.factory;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.channel.ChannelRepositoryInMemory;
import com.sprint.mission.discodeit.db.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.db.user.UserRepositoryInMemory;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import com.sprint.mission.discodeit.service.user.UserService;

/**
 *  빈으로 관리되는 스프링과 다르게 정확히 어떻게 구현해야하는지 알기가 어려웠음
 */
public class ApplicationInMemoryFactory implements AppFactory {
    private static ApplicationInMemoryFactory INSTANCE;

    private UserRepository userRepository;
    private UserService userService;

    private ChannelRepository channelRepository;
    private ChannelService channelService;

    private DirectMessageService messageService;
    private DirectMessageRepository messageRepository;

    private ApplicationInMemoryFactory() {}

    public static synchronized ApplicationInMemoryFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationInMemoryFactory();
        }
        return INSTANCE;
    }

    @Override
    public UserService getUserService() {
        if (userService == null) {
            userService = JCFUserService.getInstance(getUserRepository());
        }
        return userService;
    }

    @Override
    public ChannelService getChannelService() {
        if (channelService == null) {
            channelService = JCFChannelService.getInstance(getUserRepository(), getChannelRepository());
        }
        return channelService;
    }

    @Override
    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = UserRepositoryInMemory.getInstance();
        }
        return userRepository;
    }

    @Override
    public ChannelRepository getChannelRepository() {
        if (channelRepository == null) {
            channelRepository = ChannelRepositoryInMemory.getChannelRepositoryInMemory();
        }
        return channelRepository;
    }

    @Override
    public DirectMessageService getMessageService() {
        return null;
    }

    @Override
    public DirectMessageRepository getMessageRepository() {
        return null;
    }
}
