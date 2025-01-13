package com.sprint.mission.discodeit.factory.jcf;

import com.sprint.mission.discodeit.factory.MessageServiceFactory;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.proxy.MessageServiceProxy;

public class JCFMessageServiceFactory implements MessageServiceFactory {
    @Override
    public MessageService createMessageService() {
        return new MessageServiceProxy(new JCFMessageService());
    }
}
