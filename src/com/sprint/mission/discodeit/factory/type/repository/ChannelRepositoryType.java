package com.sprint.mission.discodeit.factory.type.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.proxy.ChannelRepositoryProxy;

import java.util.function.Supplier;

public enum ChannelRepositoryType {
    JCF(JCFChannelRepository::new),
    FILE(FileChannelRepository::new)
    ;

    private final Supplier<ChannelRepository> supplier;

    ChannelRepositoryType(Supplier<ChannelRepository> supplier) {
        this.supplier = supplier;
    }

    public ChannelRepository create() {
        return new ChannelRepositoryProxy(supplier.get());
    }
}
