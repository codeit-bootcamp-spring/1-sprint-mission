package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReadStatus findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusMap.get(id);
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

