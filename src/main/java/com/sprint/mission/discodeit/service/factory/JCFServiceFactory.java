package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JCFServiceFactory implements ServiceFactory{
    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfChannelService;
    private final JCFMessageService jcfMessageService;

    private JCFServiceFactory() {
        this.jcfUserService = JCFUserService.getInstance();
        this.jcfChannelService = JCFChannelService.getInstance();
        this.jcfMessageService = JCFMessageService.getInstance();
    }

    public static JCFServiceFactory getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final JCFServiceFactory INSTANCE = new JCFServiceFactory();
    }

    @Override
    public UserService getUserService() {
        return this.jcfUserService;
    }

    @Override
    public ChannelService getChannelService() {
        return this.jcfChannelService;
    }

    @Override
    public MessageService getMessageService() {
        return this.jcfMessageService;
    }
}
