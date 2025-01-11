package com.sprint.mission.discodeit.entity.user.db;

import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.ArrayList;
import java.util.List;

public class ParticipatedChannel {

    private final List<Channel> participatedChannels;

    private ParticipatedChannel(List<Channel> participatedChannels) {
        this.participatedChannels = participatedChannels;
    }

    public static ParticipatedChannel from(List<Channel> participatedChannels) {
        return new ParticipatedChannel(participatedChannels);
    }

    public Channel createChannel(String channelName) {
        var channel = Channel.of(channelName);
        participatedChannels.add(channel);
        return channel;
    }
}
