package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<User, List<Channel>> channelData = new HashMap<>();

    @Override
    public Channel saveChannel(Channel channel) {
        channelData.computeIfAbsent(channel.getUser(), k -> new ArrayList<>()).add(channel);
        return channel;
    }

    @Override
    public void deleteChannel(Channel channel) {
        List<Channel> userChannels = channelData.get(channel.getUser());
        if (userChannels != null) {
            userChannels.remove(channel);
            if (userChannels.isEmpty()) {
                channelData.remove(channel.getUser());
            }
        }
    }

    @Override
    public List<Channel> printUser(User user) {
        return channelData.getOrDefault(user, new ArrayList<>());
    }

    @Override
    public List<Channel> printAllChannel() {
        List<Channel> Channels = new ArrayList<>();
        for (List<Channel> userChannels : channelData.values()) {
            Channels.addAll(userChannels);
        }
        return Channels;
    }

}