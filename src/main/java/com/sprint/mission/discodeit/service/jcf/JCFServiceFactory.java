package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

public class JCFServiceFactory implements ServiceFactory {
    private static final JCFUserService userService = JCFUserService.getInstance();
    private static final JCFChannelService channelService = JCFChannelService.getInstance();
    private static final JCFMessageService messageService = JCFMessageService.getInstance(userService, channelService);

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}