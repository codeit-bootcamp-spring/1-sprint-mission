package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public Channel saveChannel(Channel channel) {
        channelData.put(channel.getId(), channel);
        return channelData.get(channel.getId());
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public List<Channel> findAccessibleChannels(User user) {
        return channelData.values().stream()
                .filter(channel ->
                        channel.isPublic() || (channel.isPrivate() && channel.isUserInChannel(user)))
                .toList();
    }

    @Override
    public void removeChannel(UUID channelId) {
        channelData.remove(channelId);
    }
}
