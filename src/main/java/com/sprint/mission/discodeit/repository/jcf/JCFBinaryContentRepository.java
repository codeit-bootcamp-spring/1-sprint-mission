package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        data.put(content.getId(), content);
        return content;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(content -> content.getUserId() != null && content.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return data.values().stream()
                .filter(content -> content.getMessageId() != null && content.getMessageId().equals(messageId))
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return data.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        data.entrySet().removeIf(entry ->
                entry.getValue().getMessageId() != null && entry.getValue().getMessageId().equals(messageId));
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
