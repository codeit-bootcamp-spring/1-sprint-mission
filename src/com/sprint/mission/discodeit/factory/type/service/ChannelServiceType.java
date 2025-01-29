package com.sprint.mission.discodeit.factory.type.service;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.proxy.ChannelServiceProxy;

import java.util.function.Function;

public enum ChannelServiceType {
    JCF(JCFChannelService::new),
    FILE(FileChannelService::new)
    ;

    private final Function<ChannelRepository, ChannelService> function;

    ChannelServiceType(Function<ChannelRepository, ChannelService> function) {
        this.function = function;
    }

    public ChannelService create(ChannelRepository channelRepository) {
        return new ChannelServiceProxy(function.apply(channelRepository));
    }
}
