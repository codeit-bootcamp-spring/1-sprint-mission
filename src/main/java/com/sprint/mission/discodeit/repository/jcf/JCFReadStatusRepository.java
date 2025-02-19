package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
        this.data = new HashMap<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Map<UUID, ReadStatus> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
