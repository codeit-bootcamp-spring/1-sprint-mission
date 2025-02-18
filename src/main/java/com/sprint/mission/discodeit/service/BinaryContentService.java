package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent createBinaryContent(MultipartFile multipartFile);
    BinaryContent createBinaryContent(BinaryContent binaryContent);

    BinaryContent findBinaryContentById(UUID binaryContentId);

    List<BinaryContent> findAllBinaryContentsById(List<UUID> binaryContentIdList);

    void deleteBinaryContent(UUID binaryContentId);
}
