package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {

    BinaryContent createBinaryContent(BinaryContent binaryContentToCreate);

    BinaryContent findBinaryContentById(UUID key);

    List<BinaryContent> findAllBinaryContentByIdIn(List<UUID> keys);

    BinaryContent deleteBinaryContentById(UUID key);
}
