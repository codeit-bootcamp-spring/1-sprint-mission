package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.data.ContentType;

import java.util.UUID;

public record BinaryContentDTO(
        String filename,
        String fileType,
        byte[] data
) { }
