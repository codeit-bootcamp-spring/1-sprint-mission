package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    //
    List<BinaryContent> findAllByUserId(UUID userId);
    List<BinaryContent> findAllByMessageId(UUID messageId);
    Optional<BinaryContent> findById(UUID binaryContentId);
    List<BinaryContent> findAllByIdIn(List<UUID>ids);  //아이디 목록 기준 조회
    //
    void deleteByMessageId(UUID messageId);
    void delete(BinaryContent binaryContent);
    void deleteByUserId(UUID userId);
    //
    boolean existsByUserId(UUID userId);
}
