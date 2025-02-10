package com.sprint.mission.discodeit.factory.type.service;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.proxy.MessageServiceProxy;

import java.util.function.Function;

public enum MessageServiceType {
    JCF(JCFMessageService::new),
    FILE(FileMessageService::new)
    ;

    private final Function<MessageRepository, MessageService> function;

    MessageServiceType(Function<MessageRepository, MessageService> function) {
        this.function = function;
    }

    public MessageService create(MessageRepository messageRepository) {
        return new MessageServiceProxy(function.apply(messageRepository));
    }
}
