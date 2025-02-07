package com.sprint.mission.discodeit.repository.domain.file;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Profile("File")
public class BinaryContentFileRepositoryImpl implements BinaryContentRepository {

    @Override
    public BinaryContent save(BinaryContentDto binaryContentDto) {
        return null;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return null;
    }

    @Override
    public List<BinaryContent> findByDomainId(UUID domainId) {
        return List.of();
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void deleteByDomainId(UUID domainId) {

    }

    @Override
    public void update(UUID id, BinaryContent binaryContent) {

    }
}
