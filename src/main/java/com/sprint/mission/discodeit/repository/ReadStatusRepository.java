package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus createReadStatus(ReadStatus readStatusToCreate);

    ReadStatus findReadStatusById(UUID key);

    List<ReadStatus> findAllReadStatus();

    ReadStatus updateReadStatus(UUID key, ReadStatus readStatusToUpdate);

    ReadStatus deleteReadStatusById(UUID key);
}
