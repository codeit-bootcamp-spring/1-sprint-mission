package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

//    BinaryContent save(MultipartFile file);

    BinaryContent save(MultipartFile file, UUID userId, UUID requestId);

    BinaryContent saveSync(MultipartFile file);

    BinaryContentResponse findById(UUID id);

    List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);

    void deleteById(UUID id);
}
