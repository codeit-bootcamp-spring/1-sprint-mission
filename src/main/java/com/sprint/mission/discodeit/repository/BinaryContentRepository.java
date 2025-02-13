package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    boolean existsById(UUID id);
    void deleteById(UUID id);

//    BinaryContent save(BinaryContent binaryContent);
//    Optional<BinaryContent> findProfileByUserId(UUID userId);
//    Optional<BinaryContent> findByContentId(UUID contentId);
//    List<BinaryContent> findAllByUserId(UUID userId);
//    List<BinaryContent> findByAllMessageId(UUID messageId);
//    void deleteByUserId(UUID userId);
//    void deleteByMessageId(UUID messageId);
//    void deleteByContentId(UUID contentId);

}
