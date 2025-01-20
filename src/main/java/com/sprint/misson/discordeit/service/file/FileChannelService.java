package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.service.ChannelService;

import java.util.List;

public class FileChannelService implements ChannelService {
    @Override
    public Channel createChannel(String name, ChannelType type) {
        return null;
    }

    @Override
    public List<Channel> getChannels() {
        return List.of();
    }

    @Override
    public Channel getChannelByUUID(String channelId) {
        return null;
    }

    @Override
    public List<Channel> getChannelsByName(String channelName) {
        return List.of();
    }

    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return List.of();
    }

    @Override
    public Channel updateChannel(String channelId, ChannelDTO channelDTO) {
        return null;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        return false;
    }

    @Override
    public List<User> getUsersInChannel(Channel channel) {
        return List.of();
    }

    @Override
    public boolean addUserToChannel(Channel channel, User user) {
        return false;
    }

    @Override
    public boolean deleteUserFromChannel(Channel channel, User user) {
        return false;
    }

    @Override
    public boolean isUserInChannel(Channel channel, User user) {
        return false;
    }
}
