package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.data.BinaryContentData;

import java.util.Map;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final BinaryContentData binaryContentData = BinaryContentData.getInstance();

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent);
    }

    @Override
    public Map<UUID, BinaryContent> load() {
        return binaryContentData.load();
    }

    @Override
    public void delete(UUID id) {
        binaryContentData.delete(id);
    }
}
