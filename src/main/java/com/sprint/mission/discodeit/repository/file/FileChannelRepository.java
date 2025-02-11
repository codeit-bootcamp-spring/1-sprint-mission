package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fileChannelRepository")
@Primary
public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public List<Channel> findAllPrivateChannelsByUserId(UUID userId) {
        return channels.values().stream()
                .filter(channel -> !channel.isPublic() && channel.getCreatorId().equals(userId))
                .toList();
    }
}
