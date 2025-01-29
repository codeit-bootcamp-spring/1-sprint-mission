package com.sprint.mission.discodeit.factory.type.repository;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.proxy.MessageRepositoryProxy;

import java.util.function.Supplier;

public enum MessageRepositoryType {
    JCF(JCFMessageRepository::new),
    FILE(FileMessageRepository::new)
    ;

    private final Supplier<MessageRepository> supplier;

    MessageRepositoryType(Supplier<MessageRepository> supplier) {
        this.supplier = supplier;
    }

    public MessageRepository create() {
        return new MessageRepositoryProxy(supplier.get());
    }
}
