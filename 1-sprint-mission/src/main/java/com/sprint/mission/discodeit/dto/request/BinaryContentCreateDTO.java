package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record BinaryContentCreateDTO(
        UUID userId,
        String filename,
        String contentType,
        byte[] fileData
) {}
