package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.proxy.MessageRepositoryProxy;
import com.sprint.mission.discodeit.repository.proxy.UserRepositoryProxy;

import java.util.function.Supplier;

public enum MessageRepositoryFactory {
    JCF_MESSAGE_REPOSITORY_FACTORY(() -> new MessageRepositoryProxy(new JCFMessageRepository())),
    FILE_MESSAGE_REPOSITORY_FACTORY(() -> new MessageRepositoryProxy(new FileMessageRepository()))
    ;

    private final Supplier<MessageRepository> supplier;

    MessageRepositoryFactory(Supplier<MessageRepository> supplier) {
        this.supplier = supplier;
    }

    public MessageRepository createMessageRepository() {
        return supplier.get();
    }
}
