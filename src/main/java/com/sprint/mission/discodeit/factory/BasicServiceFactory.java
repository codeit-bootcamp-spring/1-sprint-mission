package com.sprint.mission.discodeit.factory;

/*

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
}*/
