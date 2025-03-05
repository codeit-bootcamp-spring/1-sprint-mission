package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<Long, ReadStatus> data;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(ReadStatus readStatus) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, readStatus);
        return id;
    }

    @Override
    public ReadStatus load(Long id) {
        return data.get(id);

    }

    @Override
    public Map.Entry<Long, ReadStatus> load(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
    }

    @Override
    public Map<Long, ReadStatus> loadAll() {
        return data;
    }

    @Override
    public void delete(Long id) {
        if (data.remove(id) == null) {
            // TODO: CUSTOM ERROR
            throw new IllegalArgumentException("삭제할 Read Status가 존재하지 않습니다.");
        }
    }

    @Override
    public void update(Long id, ReadStatus readStatus) {
        data.put(id, readStatus);
    }

    @Override
    public Map<Long, ReadStatus> findAllByUserId(UUID userId) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Instant findUpToDateReadTimeByChannelId(UUID uuid) {
        return loadAll().values().stream()
                .filter(entry -> entry.getChannelId().equals(uuid))
                .map(ReadStatus::getLastReadAt)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getChannelId().equals(channelId))
                .map(Map.Entry::getKey)
                .forEach(this::delete);
    }
}
