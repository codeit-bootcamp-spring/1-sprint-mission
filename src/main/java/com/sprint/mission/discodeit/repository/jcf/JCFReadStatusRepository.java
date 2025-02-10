package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        data = new HashMap<>();
    }

    @Override
    public UUID save(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus findOne(UUID id) {
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }


    @Override
    public Optional<ReadStatus> findByUserIdAndChannlId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(
                        readStatus -> readStatus.getUserId().equals(userId) &&
                                readStatus.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public UUID update(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus.getId();
    }

    @Override
    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }


}
