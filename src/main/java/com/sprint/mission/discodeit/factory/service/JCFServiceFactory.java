package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JCFServiceFactory implements ServiceFactory {

    private static JCFServiceFactory instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    private JCFServiceFactory() {
        this.userService = new JCFUserService();
        this.channelService = new JCFChannelService(userService);
        this.messageService = new JCFMessageService(channelService, userService);
    }

    public static JCFServiceFactory getInstance() {
        if (instance == null) {
            instance = new JCFServiceFactory();
        }
        return instance;
    }

}
