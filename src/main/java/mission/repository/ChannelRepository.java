package mission.repository;

import mission.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel findById(UUID id);
    List<Channel> findAll();

    Channel findByName(String channelName);

    Channel updateChannelName(Channel findChannel, String newName);
}
