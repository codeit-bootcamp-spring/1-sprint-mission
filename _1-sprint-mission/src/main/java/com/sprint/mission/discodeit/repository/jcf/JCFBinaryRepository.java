package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryRepository implements BinaryContentRepository {
    private final Map<UUID,BinaryContent> binaryContentData;
    public JCFBinaryRepository() {
        this.binaryContentData = new HashMap<>();
    }
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        return Optional.ofNullable(binaryContentData.get(binaryContentId));
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> binaryContentIds) {
        return binaryContentIds.stream()
                .map(b->binaryContentData.get(b))
                .toList();
    }

    @Override
    public void deleteById(UUID binaryContentId) {
        binaryContentData.remove(binaryContentId);
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        return binaryContentData.containsKey(binaryContentId);
    }
}
