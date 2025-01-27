package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JCFServiceFactory implements ServiceFactory {
    private static JCFServiceFactory instance;
    private final UserService userService;
    private final MessageService messageService;
    private final ChannelService channelService;

    private JCFServiceFactory() {
        this.userService = new JCFUserService();
        this.channelService = new JCFChannelService();
        this.messageService = new JCFMessageService(userService, channelService);
    }

    public static JCFServiceFactory getInstance() {
        if (instance == null) {
            instance = new JCFServiceFactory();
        }
        return instance;
    }
    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }

    @Override
    public ChannelService getChannelService() {
        return channelService;
    }
}
