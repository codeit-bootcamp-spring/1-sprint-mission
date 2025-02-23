package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JcfReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> data = new ConcurrentHashMap<>();

    @Override
    public ReadStatus createReadStatus(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> all = findAll();
        List<ReadStatus> allByUserId= new ArrayList<>();
        for (ReadStatus readStatus : all) {
            if(readStatus.getUserId().equals(userId)) {
                allByUserId.add(readStatus);
                return allByUserId;
            }
        }
        return null;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> all = findAll();
        List<ReadStatus> allByChannelId= new ArrayList<>();
        for (ReadStatus readStatus : all) {
            if(readStatus.getChannelId().equals(channelId)) {
                allByChannelId.add(readStatus);
                return allByChannelId;
            }
        }
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }
}
