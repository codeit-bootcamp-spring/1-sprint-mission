package com.spirnt.mission.discodeit.repository.jcf;

import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        data = new HashMap<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId) &&
                        readStatus.getUserId().equals(userId))
                .findAny();
    }

    @Override
    public Map<UUID, ReadStatus> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
