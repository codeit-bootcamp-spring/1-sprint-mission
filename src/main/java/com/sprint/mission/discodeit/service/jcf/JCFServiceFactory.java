package com.sprint.mission.discodeit.service.jcf;

public class JCFServiceFactory {
    private static final JCFUserService userService = JCFUserService.getInstance();
    private static final JCFChannelService channelService = JCFChannelService.getInstance();
    private static final JCFMessageService messageService = JCFMessageService.getInstance(userService, channelService);

    public static JCFUserService getUserService() {
        return userService;
    }

    public static JCFChannelService getChannelService() {
        return channelService;
    }

    public static JCFMessageService getMessageService() {
        return messageService;
    }
}