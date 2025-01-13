package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.jcf.JCFChannelServiceFactory;

public enum ChannelServiceFactoryType {
    JCF(new JCFChannelServiceFactory()),
    ;

    private final ChannelServiceFactory factory;

    ChannelServiceFactoryType(ChannelServiceFactory factory) {
        this.factory = factory;
    }

    public ChannelServiceFactory getFactory() {
        return factory;
    }
}
