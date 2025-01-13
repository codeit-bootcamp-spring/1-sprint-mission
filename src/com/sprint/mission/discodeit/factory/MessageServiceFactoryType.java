package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.jcf.JCFMessageServiceFactory;

public enum MessageServiceFactoryType {
    JCF(new JCFMessageServiceFactory()),
    ;

    private final MessageServiceFactory factory;

    MessageServiceFactoryType(MessageServiceFactory factory) {
        this.factory = factory;
    }

    public MessageServiceFactory getFactory() {
        return factory;
    }
}
