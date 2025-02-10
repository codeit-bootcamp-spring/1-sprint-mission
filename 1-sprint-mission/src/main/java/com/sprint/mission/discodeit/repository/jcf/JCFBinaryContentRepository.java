package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> storage = new ConcurrentHashMap<>();


    @Override
    public void save(BinaryContent binaryContent) {
        storage.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        return Optional.ofNullable(storage.get(binaryContentId));
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return storage.values().stream()
                .filter(content -> content.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return storage.values().stream()
                .filter(content -> content.getMessageId() != null && content.getMessageId().equals(messageId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(storage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        storage.remove(binaryContent.getId());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.values().removeIf(content -> content.getUserId().equals(userId));
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        storage.values()
                .removeIf(content -> content.getMessageId() != null && content.getMessageId().equals(messageId));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return storage.values().stream()
                .anyMatch(content -> content.getUserId().equals(userId));
    }
}
