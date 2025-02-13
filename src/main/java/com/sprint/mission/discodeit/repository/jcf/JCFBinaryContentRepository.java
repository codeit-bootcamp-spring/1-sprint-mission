package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> store;
    
    public JCFBinaryContentRepository(){
        this.store = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        store.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.store.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return this.store.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.store.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.store.remove(id);
    }
}

