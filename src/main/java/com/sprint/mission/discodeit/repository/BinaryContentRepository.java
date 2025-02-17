package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent getBinaryContent(BinaryContentType type, UUID binaryContentId);
    boolean deleteBinaryContent(UUID binaryContentId);
    boolean saveBinaryContent(BinaryContent binaryContent);
    boolean isBinaryContentExist(BinaryContentType type, UUID binaryContentId);
}