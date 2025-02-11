package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Map;

public class BasicChannelService implements ChannelService {
    @Override
    public void createChannel(Channel channel) {

    }

    @Override
    public void deleteChannel(Channel channel, User admin) {

    }

    @Override
    public void updateChannel(Channel channel, String newName, User admin) {

    }

    @Override
    public List<Map<String, String>> getAllChannel() {
        return List.of();
    }

    @Override
    public List<String> getAllMemberNames(Channel channel) {
        return List.of();
    }

    @Override
    public boolean channelExits(Channel channel) {
        return false;
    }
}
