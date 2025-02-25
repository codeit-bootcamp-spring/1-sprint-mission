package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent saveFile(MultipartFile file) throws IOException;

    BinaryContent find(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> uuidList);

    List<BinaryContent> findAll();

    void delete(UUID id);
}
