package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContents = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContents.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContents.get(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            BinaryContent content = binaryContents.get(id);
            if (content != null) {
                result.add(content);
            }
        }
        return result;
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryContents.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        binaryContents.remove(id);
    }
}