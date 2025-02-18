package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {

    BinaryContent save(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    Map<UUID, BinaryContent> load();
    void delete(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> uuidList);
}
