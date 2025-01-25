package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.jcf.BasicJCFChannelService;
import com.sprint.mission.discodeit.service.basic.jcf.BasicJCFMessageService;
import com.sprint.mission.discodeit.service.basic.jcf.BasicJCFUserService;


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
        this.userService = new BasicJCFUserService(userRepository);
        this.channelService = new BasicJCFChannelService(channelRepository);
        this.messageService = new BasicJCFMessageService(messageRepository);
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