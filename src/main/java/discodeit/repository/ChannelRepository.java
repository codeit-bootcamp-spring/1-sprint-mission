package discodeit.repository;

import discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    List<Channel> findAllByUserId(UUID userId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
