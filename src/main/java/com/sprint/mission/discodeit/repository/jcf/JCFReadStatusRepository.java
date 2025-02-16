package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Scope("singleton")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return data.get(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(readstatus -> readstatus.isSameChannelId(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readstatus -> readstatus.isSameUserId(userId))
                .toList();
    }

    @Override
    public void delete(UUID readStatusId) {
        data.remove(readStatusId);
    }

    @Override
    public boolean existsById(UUID readStatusId) {
        return data.containsKey(readStatusId);
    }
}
