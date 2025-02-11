package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return null;
    }

    @Override
    public Map<UUID, ReadStatus> load() {
        return Map.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public Instant findLatestTimeByChannelId(UUID channeId) {
        return null;
    }
}
