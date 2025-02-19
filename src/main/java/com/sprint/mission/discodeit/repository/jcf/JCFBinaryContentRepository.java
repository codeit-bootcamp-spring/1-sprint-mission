package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Profile("jcf")
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> repository;

    public JCFBinaryContentRepository() {
        this.repository = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        repository.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        return repository.values().stream()
                .filter(content -> content.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return repository.values().stream().toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        repository.remove(uuid);
    }
}
