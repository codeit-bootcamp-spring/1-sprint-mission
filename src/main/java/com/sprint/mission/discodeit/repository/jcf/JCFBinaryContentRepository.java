package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    HashMap<UUID, BinaryContent> binaryContentsMap = new HashMap<UUID, BinaryContent>();

    @Override
    public BinaryContent getBinaryContent(BinaryContentType type, UUID binaryContentId) {
        return binaryContentsMap.get(binaryContentId);
    }

    @Override
    public boolean deleteBinaryContent(BinaryContentType type, UUID binaryContentId) {
        return binaryContentsMap.remove(binaryContentId)!=null;
    }

    @Override
    public boolean saveBinaryContent(BinaryContent binaryContent) {
        return binaryContentsMap.put(binaryContent.getId(), binaryContent)!=null;
    }

    @Override
    public boolean isBinaryContentExist(BinaryContentType type, UUID binaryContentId) {
        return binaryContentsMap.get(binaryContentId)!=null;
    }
}
