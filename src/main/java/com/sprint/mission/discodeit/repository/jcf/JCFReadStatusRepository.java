package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public Optional<ReadStatus> save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        return Optional.of(readStatus);
    }

    @Override
    public Optional<ReadStatus> findByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusMap.remove(readStatusId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusMap.values().removeIf(rs -> rs.getChannelId().equals(channelId));
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId));
    }
}

