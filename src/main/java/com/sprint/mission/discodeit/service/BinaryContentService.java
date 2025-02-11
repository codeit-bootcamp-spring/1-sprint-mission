package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    void storeBinaryContent(UUID ownerId, byte[] data, String fileType);
    Optional<BinaryContent> getBinaryContent(UUID id);
    boolean deleteBinaryContent(UUID id); // ✅ 파일 삭제 메서드 추가
}
