package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContent createUserProfileFile(MultipartFile file, UUID userId);
    BinaryContent createMessageFile(MultipartFile file, UUID messageId);
    BinaryContent updateUserProfileFile(MultipartFile file, UUID userId);
    BinaryContent findByIdOrThrow(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
    void deleteAllByMessageId(UUID messageId);
}
