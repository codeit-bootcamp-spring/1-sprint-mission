package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<String, BinaryContent> dataStore = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent content) {
        dataStore.put(content.getId(), content);
        return content;
    }

    @Override
    public void deleteById(String id) {
        dataStore.remove(id);
    }

    @Override
    public Optional<BinaryContent> findByUserId(String id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(dataStore.values());
    }
}