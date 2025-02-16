package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<BinaryContent> findByUserIdAndType(UUID userId, BinaryContentType binaryContentType) {
        return Optional.empty();
    }

    @Override
    public List<BinaryContent> findAllContentsByType(UUID typeId) {
        return List.of();
    }

    @Override
    public void delete(BinaryContent binaryContent) {

    }
}
