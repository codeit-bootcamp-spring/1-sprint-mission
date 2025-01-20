package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.proxy.ChannelRepositoryProxy;
import com.sprint.mission.discodeit.repository.proxy.UserRepositoryProxy;

import java.util.function.Supplier;

public enum ChannelRepositoryFactory {
    JCF_CHANNEL_REPOSITORY_FACTORY(() -> new ChannelRepositoryProxy(new JCFChannelRepository())),
    FILE_CHANNEL_REPOSITORY_FACTORY(() -> new ChannelRepositoryProxy(new FileChannelRepository()))
    ;

    private final Supplier<ChannelRepository> supplier;

    ChannelRepositoryFactory(Supplier<ChannelRepository> supplier) {
        this.supplier = supplier;
    }

    public ChannelRepository createChannelRepository() {
        return supplier.get();
    }
}
