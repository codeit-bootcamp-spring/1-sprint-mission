package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        data = new HashMap<>();
    }

    @Override
    public UUID save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus.getId();
    }

    @Override
    public ReadStatus findOne(UUID id) {
        return data.get(id);
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
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
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

    @Override
    public void deleteByChannelId(UUID channelId){
        findAllByChannelId(channelId)
                .forEach(rS -> delete(rS.getId()));
    }


}
