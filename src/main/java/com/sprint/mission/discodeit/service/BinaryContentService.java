package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponse createUserProfileFile(MultipartFile file, UUID userId);
    BinaryContentResponse createMessageFile(MultipartFile file, UUID messageId);
    BinaryContentResponse updateUserProfileFile(MultipartFile file, UUID userId);
    BinaryContentResponse findByIdOrThrow(UUID id);
    BinaryContentResponse findByUserId(UUID userId);
    List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
    void deleteAllByMessageId(UUID messageId);
}
