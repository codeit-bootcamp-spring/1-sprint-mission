package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JcfReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public ReadStatus createReadStatus(UUID id, ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findByUserId(UUID userId) {
        List<ReadStatus> all = findAll();
        for (ReadStatus readStatus : all) {
            if(readStatus.getUserId().equals(userId)) {
                return Optional.of(readStatus);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }
}
