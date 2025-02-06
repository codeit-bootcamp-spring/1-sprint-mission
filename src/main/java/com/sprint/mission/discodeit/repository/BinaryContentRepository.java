package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent content);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAllByUserId(UUID userId);

    List<BinaryContent> findAllByMessageId(UUID messageId);

    void deleteById(UUID id); //  개별 파일 삭제

    void deleteByMessageId(UUID messageId); //  특정 메시지에 속한 모든 파일 삭제
}
