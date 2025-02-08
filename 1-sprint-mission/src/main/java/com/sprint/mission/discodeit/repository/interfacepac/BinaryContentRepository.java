package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    //
    byte[]findByUserId(UUID userId);
    List<BinaryContent> findByMessageId(UUID messageId);
    Optional<BinaryContent> findById(UUID binaryContentId);
    List<BinaryContent> findAllByIdIn(List<UUID>ids);  //아이디 목록 기준 조회
    //
    void deleteByMessageId(UUID messageId);
    void delete(BinaryContent binaryContent);
}
