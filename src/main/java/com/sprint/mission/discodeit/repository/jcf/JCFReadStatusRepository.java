package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.data.ReadStatusData;

import java.util.Map;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final ReadStatusData readStatusData = ReadStatusData.getInstance();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusData.put(readStatus);
    }

    @Override
    public Map<UUID, ReadStatus> load() {
        return readStatusData.load();
    }

    @Override
    public void delete(UUID id) {
        readStatusData.delete(id);
    }
}
