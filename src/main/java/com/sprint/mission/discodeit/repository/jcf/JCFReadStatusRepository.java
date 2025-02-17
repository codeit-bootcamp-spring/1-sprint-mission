package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> readStatuses = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(readStatuses.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatuses.values().stream() // readStatuses의 values를 Collections로 들고온다
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatuses.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID readStatusId) {
        readStatuses.remove(readStatusId);
    }
}
