package mission.repository;

import mission.entity.Channel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel findById(UUID id);
    Set<Channel> findAll();

    Channel findByName(String channelName);

    Channel updateChannelName(Channel updatingChannel);
}
