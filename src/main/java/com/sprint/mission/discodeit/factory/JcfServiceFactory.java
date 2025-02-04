package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class JcfServiceFactory implements ServiceFactory {
    JcfUserService jcfUserService = null;
    JcfChannelService jcfchannelService = null;
    JcfMessageService jcfMessageService = null;

    public JcfServiceFactory() {
        super();
        jcfUserService = JcfUserService.getInstance();
        jcfchannelService = JcfChannelService.getInstance();
        jcfMessageService = JcfMessageService.getInstance();
        jcfchannelService.setJcfMessageService(jcfMessageService);
        jcfUserService.setJcfMessageService(jcfMessageService);
    }

    @Override
    public UserService getUserService() {
        return jcfUserService;
    }

    @Override
    public ChannelService getChannelService() {
        return jcfchannelService;
    }

    @Override
    public MessageService getMessageService() {
        return jcfMessageService;
    }
}
