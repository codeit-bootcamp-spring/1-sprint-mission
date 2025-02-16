package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return null;
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return Optional.empty();
    }

    @Override
    public void delete(UUID id) {

    }
}
