package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.entity.data.ContentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

    BinaryContent save(BinaryContent binaryContent, ContentType contentType);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAll();

    void deleteById(UUID id, ContentType contentType);

}
