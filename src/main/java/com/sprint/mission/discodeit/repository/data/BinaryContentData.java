package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryContentData {
    private final Map<UUID, BinaryContent> binaryContents = new ConcurrentHashMap<>();

    private static BinaryContentData binaryContentData;

    private BinaryContentData() {
    }

    public static BinaryContentData getInstance() {

        if (binaryContentData == null) {
            binaryContentData = new BinaryContentData();
        }

        return binaryContentData;
    }

    public void put(BinaryContent binaryContent) {

        binaryContents.put(binaryContent.getId(), binaryContent);
    }

    public Map<UUID, BinaryContent> load() {

        return binaryContents;
    }

    public void delete(UUID id) {

        binaryContents.remove(id);
    }
}
