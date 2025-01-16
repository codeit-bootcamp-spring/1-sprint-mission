package com.sprint.mission.discodeit.config.factory;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.channel.ChannelRepositoryInMemory;
import com.sprint.mission.discodeit.db.message.ChannelMessage.ChannelMessageRepository;
import com.sprint.mission.discodeit.db.message.ChannelMessage.ChannelMessageRepositoryInMemory;
import com.sprint.mission.discodeit.db.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.db.message.directMessage.DirectMessageRepositoryInMemory;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.db.user.UserRepositoryInMemory;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFDirectMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.message.channelMessage.ChannelMessageService;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import com.sprint.mission.discodeit.service.user.UserService;

/**
 *  빈으로 관리되는 스프링과 다르게 정확히 어떻게 구현해야하는지 알기가 어려웠음
 */
public class ApplicationInMemoryFactory implements AppFactory {
    private static ApplicationInMemoryFactory INSTANCE;

    private final UserRepository userRepository;
    private final UserService userService;

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    private final DirectMessageService directMessageService;
    private final DirectMessageRepository directMessageRepository;

    private final ChannelMessageService channelMessageService;
    private final ChannelMessageRepository channelMessageRepository;

    private static class SingleInstanceHolder {
        private static final ApplicationInMemoryFactory INSTANCE =
                new ApplicationInMemoryFactory();
    }
    private ApplicationInMemoryFactory() {
        this.userRepository = UserRepositoryInMemory.getInstance();
        this.channelRepository = ChannelRepositoryInMemory.getChannelRepositoryInMemory();
        this.directMessageRepository = DirectMessageRepositoryInMemory.getInstance();
        this.channelMessageRepository = ChannelMessageRepositoryInMemory.getInstance();

        this.userService = JCFUserService.getInstance(userRepository);
        this.channelService = JCFChannelService.getInstance(userRepository, channelRepository);
        this.directMessageService = JCFDirectMessageService.getInstance(directMessageRepository, userRepository);
        this.channelMessageService = JCFChannelMessageService.getInstance(userRepository, channelRepository, channelMessageRepository);
    }

    public static synchronized ApplicationInMemoryFactory getInstance() {
        return SingleInstanceHolder.INSTANCE;
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
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public ChannelRepository getChannelRepository() {
        return channelRepository;
    }

    @Override
    public DirectMessageService getDirectMessageService() {
        return directMessageService;
    }

    @Override
    public DirectMessageRepository getDirectMessageRepository() {
        return directMessageRepository;
    }

    @Override
    public ChannelMessageRepository getChannelMessageRepository() {
        return channelMessageRepository;
    }

    @Override
    public ChannelMessageService getChannelMessageService() {
        return channelMessageService;
    }
}
