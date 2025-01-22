package com.sprint.mission.discodit;

import com.sprint.mission.discodit.Service.JCF.JcfChannelService;
import com.sprint.mission.discodit.Service.JCF.JcfMessageService;
import com.sprint.mission.discodit.Service.JCF.JcfUserService;

public class Factory {
    private final JcfUserService jcfUserService;
    private final JcfChannelService jcfchannelService;
    private final JcfMessageService jcfMessageService;

    public Factory() {
        this.jcfUserService = JcfUserService.getInstance();
        this.jcfchannelService = JcfChannelService.getInstance();
        this.jcfMessageService = JcfMessageService.getInstance();
        this.jcfchannelService.setJcfMessageService(jcfMessageService);
        this.jcfUserService.setJcfMessageService(jcfMessageService);
    }

    public JcfUserService getJcfUserService() {
        return jcfUserService;
    }

    public JcfChannelService getJcfchannelService() {
        return jcfchannelService;
    }

    public JcfMessageService getJcfMessageService() {
        return jcfMessageService;
    }
}
