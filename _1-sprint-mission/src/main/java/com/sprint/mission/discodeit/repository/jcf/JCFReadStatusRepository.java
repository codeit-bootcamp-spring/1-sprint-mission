package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
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
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(readStatusData.get(readStatusId));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusData.values().stream()
                .filter(rs->rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusData.values().stream()
                .filter(rs->rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusData.values().stream()
                .filter(rs->rs.getChannelId().equals(channelId))
                .toList();
    }


    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusData.values());
    }
    @Override
    public boolean existsById(UUID id) {
        return this.readStatusData.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.readStatusData.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId).forEach(readStatus -> this.deleteById(readStatus.getId()));
    }


}
