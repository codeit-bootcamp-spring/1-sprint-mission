package com.sprint.mission.discodeit.dto.binary;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContentCreateRequest {
    private UUID userId;
    private byte[] data;
    private UUID messageId;
}
