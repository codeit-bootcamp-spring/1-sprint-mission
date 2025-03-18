package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentRequest request);
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAll();
    List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
    Optional<BinaryContentRequest> resolveProfileRequest(MultipartFile file);
}
