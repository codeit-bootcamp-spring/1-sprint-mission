package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
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
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteByUserIdAndChannelId(UUID userId, UUID channelId) {
        data.entrySet().removeIf(entry ->
                entry.getValue().getUserId().equals(userId) &&
                        entry.getValue().getChannelId().equals(channelId));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .distinct()
                .toList();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId));
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        data.entrySet().removeIf(entry -> entry.getValue().getMessageId().equals(messageId));
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
