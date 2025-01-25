package mission.repository;

import mission.entity.Channel;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel create(Channel channel) throws IOException;
    Set<Channel> findAll() throws IOException;
    Optional<Channel> findById(UUID id) throws IOException, ClassNotFoundException;
    //Channel updateChannelName(Channel updatingChannel);
    void deleteById(UUID id);
}
