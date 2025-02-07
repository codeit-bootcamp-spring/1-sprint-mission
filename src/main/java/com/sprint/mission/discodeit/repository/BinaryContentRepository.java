package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContentDto binaryContentDto);
    BinaryContent findById(UUID id);
    List<BinaryContent> findByDomainId(UUID domainId);
    List<BinaryContent> findAll();
    void delete(UUID id);
    void deleteByDomainId(UUID domainId);
    void update(UUID id, BinaryContent binaryContent);
}
