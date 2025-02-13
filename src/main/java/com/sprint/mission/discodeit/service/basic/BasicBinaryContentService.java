package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service("basicBinaryContentService")
public class BasicBinaryContentService implements BinaryContentService {

    private final Map<UUID, BinaryContent> binaryStorage = new ConcurrentHashMap<>();

    @Override
    public void storeBinaryContent(UUID ownerId, byte[] data, String fileType) {
        UUID fileId = UUID.randomUUID();
        binaryStorage.put(fileId, new BinaryContent(fileId, ownerId, data, fileType));
    }

    @Override
    public Optional<BinaryContent> getBinaryContent(UUID id) {
        return Optional.ofNullable(binaryStorage.get(id));
    }

    @Override
    public boolean deleteBinaryContent(UUID id) {
        return binaryStorage.remove(id) != null;
    }
}
