package com.sprint.mission.discodeit.factory.jcf;

import com.sprint.mission.discodeit.factory.ChannelServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.proxy.ChannelServiceProxy;

public class JCFChannelServiceFactory implements ChannelServiceFactory {
    @Override
    public ChannelService createChannelService() {
        return new ChannelServiceProxy(new JCFChannelService());
    }
}
