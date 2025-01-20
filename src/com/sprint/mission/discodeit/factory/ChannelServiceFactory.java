package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.proxy.ChannelServiceProxy;

import java.util.function.Supplier;

public enum ChannelServiceFactory {
    JCF_CHANNEL_SERVICE_FACTORY(() -> new ChannelServiceProxy(new JCFChannelService())),
    FILE_CHANNEL_SERVICE_FACTORY(() -> new ChannelServiceProxy(new FileChannelService()))
    ;

    private final Supplier<ChannelService> supplier;

    ChannelServiceFactory(Supplier<ChannelService> supplier) {
        this.supplier = supplier;
    }

    public ChannelService createChannelService() {
        return supplier.get();
    }
}
