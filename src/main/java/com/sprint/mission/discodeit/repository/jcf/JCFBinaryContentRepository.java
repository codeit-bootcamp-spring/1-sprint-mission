package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> store = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        if (binaryContent.getId() == null) {
            throw new IllegalArgumentException("BinaryContent ID cannot be null.");
        }
        store.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findByUserIdAndMessageId(UUID userId, UUID messageId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId) && content.getMessageId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByContentId(UUID contentId) {
        return store.get(contentId);
    }

    @Override
    public List<BinaryContent> findAllByContentId(List<UUID> ids) {
        return store.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(content -> content.getUserId().equals(userId));
    }

    @Override
    public void deleteByContentId(UUID contentId) {
        store.values().removeIf(content -> content.getId().equals(contentId));
    }
}

