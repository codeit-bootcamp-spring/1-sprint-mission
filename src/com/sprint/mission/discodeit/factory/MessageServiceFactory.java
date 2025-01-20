package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.proxy.MessageServiceProxy;

import java.util.function.Supplier;

public enum MessageServiceFactory {
    JCF_MESSAGE_SERVICE_FACTORY(() -> new MessageServiceProxy(new JCFMessageService())),
    FILE_MESSAGE_SERVICE_FACTORY(() -> new MessageServiceProxy(new FileMessageService()))
    ;

    private final Supplier<MessageService> supplier;

    MessageServiceFactory(Supplier<MessageService> supplier) {
        this.supplier = supplier;
    }

    public MessageService createMessageService() {
        return supplier.get();
    }
}
