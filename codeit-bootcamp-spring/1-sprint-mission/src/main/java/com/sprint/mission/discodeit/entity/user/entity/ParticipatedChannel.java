package com.sprint.mission.discodeit.entity.user.entity;

import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.ArrayList;
import java.util.List;

public class ParticipatedChannel {

    private final List<Channel> participatedChannels;

    private ParticipatedChannel(List<Channel> participatedChannels) {
        this.participatedChannels = participatedChannels;
    }

    public static ParticipatedChannel newDefault() {
        return new ParticipatedChannel(new ArrayList<Channel>(1_000));
    }

    public Channel generateFrom(String channelName) {
        var channel = Channel.of(channelName);
        participatedChannels.add(channel);
        return channel;
    }

    // 채널 찾기

    // 채널 삭제

    // 채널 수정
}
