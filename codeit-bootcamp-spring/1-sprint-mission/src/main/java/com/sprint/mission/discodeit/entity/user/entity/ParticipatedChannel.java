package com.sprint.mission.discodeit.entity.user.entity;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_PARTICIPATED_CHANNEL;

import com.sprint.mission.discodeit.common.error.user.UserException;
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

    public Channel createChannel(String channelName) {
        var channel = Channel.createFrom(channelName);
        participatedChannels.put(channel.getId(), channel);

        return channel;
    }

    public Optional<Channel> findById(UUID channelId) {
        var findChannel = participatedChannels.get(channelId);
        return Optional.ofNullable(findChannel);
    }

    // TODO 같은 이름을 가진 채널이 여러 개 있을 수 있음. 리스트로 반환 고려해야하지 않을까?
    public Optional<Channel> findByName(String name) {
        var findByNameChannel = participatedChannels.values()
                .stream()
                .filter(channel -> channel.isEqualFromNameAndNotUnregistered(name))
                .findFirst();

        return findByNameChannel;
    }

    public void changeChannelName(UUID channelId, String newName) {
        var findedChannel = findById(channelId)
                .orElseThrow(() ->
                        UserException.errorMessageAndId(USER_NOT_PARTICIPATED_CHANNEL, channelId.toString())
                );

        findedChannel.ChangeName(newName);
        participatedChannels.put(findedChannel.getId(), findedChannel);
    }
    // 채널 삭제
}
