package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<String, ReadStatus> readStatusStore = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        String key = readStatus.getUserId() + "-" + readStatus.getChannelId();
        readStatusStore.put(key, readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(String userId, String channelId) {
        return Optional.ofNullable(readStatusStore.get(userId + "-" + channelId));
    }

    @Override
    public void deleteByUserIdAndChannelId(String userId, String channelId) {
        readStatusStore.remove(userId + "-" + channelId);
    }
}