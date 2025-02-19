package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID uuid);

    List<BinaryContent> findAll();

    void deleteById(UUID uuid);
}
