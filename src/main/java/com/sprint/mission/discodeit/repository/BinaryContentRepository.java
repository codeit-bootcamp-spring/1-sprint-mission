package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentStorage = new HashMap<>();

    public void save(BinaryContent binaryContent) {
        binaryContentStorage.put(binaryContent.getId(), binaryContent);
    }

    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentStorage.get(id));
    }

    public void deleteByOwnerId(UUID ownerId) {
        binaryContentStorage.values().removeIf(content -> content.getOwnerId().equals(ownerId));
    }
}
