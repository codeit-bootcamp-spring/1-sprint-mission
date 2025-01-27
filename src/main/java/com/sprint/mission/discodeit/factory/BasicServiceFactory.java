package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


public class BasicServiceFactory implements ServiceFactory {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;

    public BasicServiceFactory() {
        // Repository 객체 생성
        this.userRepository = new JCFUserRepository();
        this.channelRepository = new JCFChannelRepository();
        this.messageRepository = new JCFMessageRepository();

        // Basic 서비스 객체 생성
        this.userService = new BasicUserService(userRepository);
        this.channelService = new BasicChannelService(channelRepository,userService);
        this.messageService = new BasicMessageService(messageRepository,userService,channelService);
    }

    @Override
    public UserService createUserService() {
        return userService;
    }

    @Override
    public ChannelService createChannelService() {
        return channelService;
    }

    @Override
    public MessageService createMessageService() {
        return messageService;
    }
}