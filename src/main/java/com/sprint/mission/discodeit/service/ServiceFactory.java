package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {
    //factory 패턴 : 의존성 주입 한 곳에서
    private static final JCFChannelService channelService = JCFChannelService.getInstance();
    private static final JCFMessageService messageService = JCFMessageService.getInstance();
    private static final JCFUserService userService = JCFUserService.getInstance();

    public static ChannelService getChannelService() {
        return channelService;
    }

    public static MessageService getMessageService() {
        return messageService;
    }

    public static UserService getUserService() {
        return userService;
    }
}
