package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID,ReadStatus> readStatusData;
    public JCFReadStatusRepository() {
        readStatusData = new HashMap<>();
    }
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusData.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public void saveAll(List<ReadStatus> readStatuses) {
        for (ReadStatus readStatus : readStatuses) {
            readStatusData.put(readStatus.getId(), readStatus);
        }
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(readStatusData.get(readStatusId));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusData.values().stream()
                .filter(rs->rs.getUser().getId().equals(userId) && rs.getChannel().getId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusData.values().stream()
                .filter(rs->rs.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusData.values());
    }

    @Override
    public void deleteById(UUID id) {
        readStatusData.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatusData.containsKey(id);
    }
}
