package com.sprint.mission.discodeit.entity.user.entity;

import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ParticipatedChannel {

    private final Map<UUID, Channel> participatedChannels;

    private ParticipatedChannel(Map<UUID, Channel> channels) {
        this.participatedChannels = channels;
    }

    public static ParticipatedChannel newDefault() {
        return new ParticipatedChannel(new HashMap<UUID, Channel>());
    }

    public Channel generateFrom(String channelName) {
        var channel = Channel.createFrom(channelName);
        participatedChannels.put(channel.getId(), channel);

        return channel;
    }

    public Optional<Channel> findById(UUID channelId) {
        var findChannel = participatedChannels.get(channelId);
        return Optional.ofNullable(findChannel);
    }

    public Optional<Channel> findByName(String name) {
        var findByNameChannel = participatedChannels.values()
                .stream()
                .filter(channel -> channel.isEqualFromName(name))
                .findFirst();

        return findByNameChannel;
    }
    // 채널 삭제

    // 채널 수정
}
