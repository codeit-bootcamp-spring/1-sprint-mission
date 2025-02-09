package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {


    UUID save(ReadStatus readStatus);
    ReadStatus findOne(UUID id);
    List<ReadStatus> findAll();
    UUID update(ReadStatus readStatus);
    UUID delete(UUID id);

}
