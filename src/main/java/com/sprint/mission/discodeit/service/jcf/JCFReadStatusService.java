package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service("jcfReadStatusService")
@RequiredArgsConstructor
public class JCFReadStatusService implements ReadStatusService {

    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public void markAsRead(UUID userId, UUID channelId, Instant timestamp) {
        ReadStatus status = new ReadStatus(userId, channelId, timestamp);
        data.put(UUID.randomUUID(), status);
    }

    @Override
    public Optional<ReadStatus> getLastRead(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> getUserReadStatuses(UUID userId) {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<ReadStatus> getChannelReadStatuses(UUID channelId) {
        List<ReadStatus> channelStatuses = new ArrayList<>();
        for (ReadStatus status : data.values()) {
            if (status.getChannelId().equals(channelId)) {
                channelStatuses.add(status);
            }
        }
        return channelStatuses;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.values().removeIf(status -> status.getUserId().equals(userId));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.values().removeIf(status -> status.getChannelId().equals(channelId));
    }
}
