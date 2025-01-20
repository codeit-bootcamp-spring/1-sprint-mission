package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicChannelService {
    public static Channel setupChannel(ChannelService channelService, Channel channelInfoToCreate) {
        return channelService.createChannel(channelInfoToCreate);
    }

    public static Channel updateChannel(ChannelService channelService, Channel channelInfoToUpdate) {
        return channelService.updateChannelById(channelInfoToUpdate.getId(), channelInfoToUpdate);
    }

    public static Channel searchChannel(ChannelService channelService, UUID key) {
        return channelService.findChannelById(key);
    }

    public static Channel removeChannel(ChannelService channelService, UUID key) {
        return channelService.deleteChannelById(key);
    }
}
