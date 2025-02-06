package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

// @Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(BinaryContent newProfile) {
        data.put(newProfile.getId(), newProfile);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.values().removeIf(user -> user.getUserId().equals(userId));
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            BinaryContent binaryContent = data.get(id);
            if (binaryContent != null) {
                result.add(binaryContent);
            }
        }
        return result;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
