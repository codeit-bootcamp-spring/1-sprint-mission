package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> store;

    public JCFReadStatusRepository() {
        store = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        store.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }
    
    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
    }
    

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(UUID readStatusId) {
        store.remove(readStatusId);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        store.values().removeIf(rs -> rs.getChannelId().equals(channelId));
    }

}

