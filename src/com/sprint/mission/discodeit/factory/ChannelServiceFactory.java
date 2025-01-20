package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.proxy.ChannelServiceProxy;

import java.util.function.Function;
import java.util.function.Supplier;

public enum ChannelServiceFactory {
    JCF_CHANNEL_SERVICE_FACTORY((channelRepository) -> new ChannelServiceProxy(new JCFChannelService(channelRepository))),
    FILE_CHANNEL_SERVICE_FACTORY((channelRepository) -> new ChannelServiceProxy(new FileChannelService(channelRepository)))
    ;

    private final Function<ChannelRepository, ChannelService> function;

    ChannelServiceFactory(Function<ChannelRepository, ChannelService> function) {
        this.function = function;
    }

    public ChannelService createChannelService(ChannelRepository channelRepository) {
        return function.apply(channelRepository);
    }
}
