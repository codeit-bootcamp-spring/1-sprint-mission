package com.sprint.mission.discodeit.config.factory;

import com.sprint.mission.discodeit.repository.file.channel.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.message.FileChannelMessageRepository;
import com.sprint.mission.discodeit.repository.file.message.FileDirectMessageRepository;
import com.sprint.mission.discodeit.repository.file.user.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.channel.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.message.ChannelMessage.ChannelMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFDirectMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.message.channelMessage.ChannelMessageService;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import com.sprint.mission.discodeit.service.user.UserService;

public class ApplicationFileFactory implements AppFactory {

    private static ApplicationFileFactory INSTANCE;


    private final UserRepository userRepository;
    private final UserService userService;

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    private final DirectMessageService directMessageService;
    private final DirectMessageRepository directMessageRepository;

    private final ChannelMessageService channelMessageService;
    private final ChannelMessageRepository channelMessageRepository;

    private static class SingleInstanceHolder {
        private static final ApplicationFileFactory INSTANCE =
                new ApplicationFileFactory();
    }

    public static synchronized ApplicationFileFactory getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }


    private ApplicationFileFactory() {
        this.userRepository = FileUserRepository.getInstance();
        this.channelRepository = FileChannelRepository.getInstance();
        this.directMessageRepository = FileDirectMessageRepository.getInstance();
        this.channelMessageRepository = FileChannelMessageRepository.getInstance();

        this.userService = JCFUserService.getInstance(userRepository);
        this.channelService = JCFChannelService.getInstance(userRepository, channelRepository);
        this.directMessageService = JCFDirectMessageService.getInstance(directMessageRepository, userRepository);
        this.channelMessageService = JCFChannelMessageService.getInstance(userRepository, channelRepository,
                channelMessageRepository);
    }


    @Override
    public UserService getUserService() {
        return userService;
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
    public ChannelService getChannelService() {
        return channelService;
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
