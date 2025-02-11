package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.data.ContentType;

import java.util.UUID;

public record ProfileImageDTO(
        ContentType contentType,
        String filename,
        String fileType,
        UUID targetUUID,
        byte[] data
) { }
