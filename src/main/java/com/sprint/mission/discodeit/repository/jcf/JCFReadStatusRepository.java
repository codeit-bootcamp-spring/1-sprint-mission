package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.HashMap;
import java.util.List;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final HashMap<String, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findById(String id) {
        return data.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @Override
    public ReadStatus findByChannelIdWithUserId(String channelId, String userId) {
        return data.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId) && rs.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(String userId) {
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(String channelId) {
        return data.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
    }

}
