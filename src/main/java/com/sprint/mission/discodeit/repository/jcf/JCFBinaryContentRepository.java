package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    // map, <id, BinaryContent>
    Map<UUID, BinaryContent> binaryContents = new HashMap<>();
    
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContents.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID bInaryContentId) {
        return Optional.ofNullable(binaryContents.get(bInaryContentId));
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContents.values().stream().toList();
    }

    @Override
    public void delete(UUID bInaryContentId) {
        binaryContents.remove(bInaryContentId);
    }
}
