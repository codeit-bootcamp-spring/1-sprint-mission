package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

public class JCFServiceFactory implements ServiceFactory {

    private static class JCFServiceFactoryHolder {
        private static final ServiceFactory INSTANCE = new JCFServiceFactory();
    }

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    private JCFServiceFactory() {
        this.userService = JCFUserService.getInstance();
        this.channelService = JCFChannelService.getInstance();

        JCFMessageService.initialize(userService, channelService);
        this.messageService = JCFMessageService.getInstance();
    }

    public static ServiceFactory getInstance() {
        return  JCFServiceFactoryHolder.INSTANCE;
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
