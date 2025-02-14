package com.sprint.mission.discodeit.dto.BinaryContent;

import com.sprint.mission.discodeit.entity.data.ContentType;

public record CreateDTO(
        ContentType contentType,
        String filename,
        String fileType,
        byte[] data
) { }
