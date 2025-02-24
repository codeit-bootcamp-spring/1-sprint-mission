package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.config.JCFRepositoryCondition;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@NoArgsConstructor
@JCFRepositoryCondition
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();

    @Override
    public ReadStatus saveReadStatus(ReadStatus readStatus) {
        readStatusData.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {
        return readStatusData.get(readStatusId);
    }

    @Override
    public Optional<ReadStatus> findReadStatusByUserId(UUID userId) {
        return readStatusData.values().stream()
                .filter(readStatus -> readStatus.getUser().getId().equals(userId))
                .findFirst();
    }

    @Override
    public void removeReadStatus(UUID readStatusId) {
        readStatusData.remove(readStatusId);
    }
}
