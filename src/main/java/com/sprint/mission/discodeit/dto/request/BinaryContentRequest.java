package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentRequest(
        BinaryContent.Type type,
        UUID belongTo,
        MultipartFile file
) {}
