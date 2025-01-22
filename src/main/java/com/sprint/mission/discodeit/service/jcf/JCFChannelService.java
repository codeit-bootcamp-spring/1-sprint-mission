package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.channelData = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public Channel createChannel(String name, UUID ownerId) throws IllegalArgumentException {
        User owner = userService.getUserById(ownerId);
        if (owner == null) {
            throw new IllegalArgumentException("Owner not found with ID: " + ownerId);
        }

        Channel channel = new Channel(name, owner);
        channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public List<User> getChannelUsers(UUID channelId) throws IllegalArgumentException {
        Channel channel = getChannelById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        List<User> users = new ArrayList<>();
        for (UUID userId : channel.getUsers()) {
            User user = userService.getUserById(userId);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void updateChannelName(UUID channelId, String newName) throws IllegalArgumentException {
        Channel channel = getChannelById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        channel.updateName(newName);
    }

    @Override
    public void deleteChannel(UUID channelId) throws IllegalArgumentException {
        if (!channelData.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        channelData.remove(channelId);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) throws IllegalArgumentException {
        Channel channel = getChannelById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        channel.addUser(userId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) throws IllegalArgumentException {
        Channel channel = getChannelById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        channel.removeUser(userId);
    }

}
