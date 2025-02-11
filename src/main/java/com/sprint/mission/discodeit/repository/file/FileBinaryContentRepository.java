package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileBinaryContentRepository implements BinaryContentRepository {
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return null;
    }

    @Override
    public Map<UUID, BinaryContent> load() {
        return Map.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> uuidList) {
        return List.of();
    }
}
