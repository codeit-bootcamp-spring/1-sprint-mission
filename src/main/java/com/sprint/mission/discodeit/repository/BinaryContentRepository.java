package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent findById(UUID id);
    BinaryContent save(MultipartFile file, UUID id);
    boolean delete(UUID id);
}
