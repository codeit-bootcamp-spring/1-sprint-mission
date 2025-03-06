package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// ReadStatus 데이터만 관리하는 일급 컬렉션
public class ReadStatusData {
    private final Map<UUID, ReadStatus> readStatuses = new ConcurrentHashMap<>();

    private static ReadStatusData readStatusData;

    private ReadStatusData() {
    }

    public static ReadStatusData getInstance() {

        if (readStatusData == null) {
            readStatusData = new ReadStatusData();
        }

        return readStatusData;
    }

    public void put(ReadStatus readStatus) {

        readStatuses.put(readStatus.getId(), readStatus);
    }

    public Map<UUID, ReadStatus> load() {

        return readStatuses;
    }

    public void delete(UUID id) {

        readStatuses.remove(id);
    }
}
