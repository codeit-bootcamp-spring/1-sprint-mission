package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return data.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return data.get(binaryContentId);
    }

    @Override
    public List<BinaryContent> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        data.remove(binaryContentId);
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        return data.containsKey(binaryContentId);
    }
}
