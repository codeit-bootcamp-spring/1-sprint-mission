package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ProfileImageDTO(
        String filename,
        String fileType,
        UUID targetUUID,
        byte[] data
) { }
