package com.sprint.mission.discodeit.repository.domain;

import com.sprint.mission.discodeit.domain.BinaryContent;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    List<BinaryContent> findByDomainId(UUID domainId);
    List<BinaryContent> findAll();
    void update(UUID id, BinaryContent binaryContent);
    void delete(UUID id);
    void deleteByDomainId(UUID domainId);

}
