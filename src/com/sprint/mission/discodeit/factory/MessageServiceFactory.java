package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.proxy.MessageServiceProxy;

import java.util.function.Function;
import java.util.function.Supplier;

public enum MessageServiceFactory {
    JCF_MESSAGE_SERVICE_FACTORY((messageRepository) -> new MessageServiceProxy(new JCFMessageService(messageRepository))),
    FILE_MESSAGE_SERVICE_FACTORY((messageRepository) -> new MessageServiceProxy(new FileMessageService(messageRepository)))
    ;

    private final Function<MessageRepository, MessageService> function;

    MessageServiceFactory(Function<MessageRepository, MessageService> function) {
        this.function = function;
    }

    public MessageService createMessageService(MessageRepository messageRepository) {
        return function.apply(messageRepository);
    }
}
