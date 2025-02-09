package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {


    UUID save(BinaryContent binaryContent);
    BinaryContent findOne(UUID id);
    List<BinaryContent> findAll();
    UUID update(BinaryContent binaryContent);
    UUID delete(UUID id);
}
