package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
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
    public Optional<BinaryContent> findByContentId(UUID contentId) {
        return store.values().stream()
                .filter(content -> content.getMessageId().equals(contentId))
                .findFirst();
    }

    @Override
    public Optional<BinaryContent> findProfileByUserId(UUID userId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId) && content.getMessageId() == null)
                .findFirst();
    }

    @Override
    public List<BinaryContent> findByAllMessageId(UUID messageId) {
        return store.values().stream()
                .filter(content -> content.getMessageId().equals(messageId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(content -> content.getUserId().equals(userId));
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        store.values().removeIf(content -> content.getMessageId().equals(messageId));
    }

    @Override
    public void deleteByContentId(UUID contentId) {
        store.values().removeIf(content -> content.getId().equals(contentId));
    }
}

