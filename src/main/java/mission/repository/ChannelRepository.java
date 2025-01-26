package mission.repository;

import mission.entity.Channel;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel create(Channel channel) throws IOException;
    Channel findById(UUID id) throws IOException, ClassNotFoundException;
    Set<Channel> findAll() throws IOException;
    //Channel updateChannelName(Channel updatingChannel);

    void delete(Channel deletingChannel);
}
