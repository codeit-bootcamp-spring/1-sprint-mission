package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> storage = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        storage.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return storage.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
