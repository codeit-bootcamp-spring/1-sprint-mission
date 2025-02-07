package com.sprint.mission.discodeit.dto.binary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class BinaryContentCreateRequest {
    private UUID userId;
    private byte[] data;
    private UUID messageId;

    public BinaryContentCreateRequest(UUID authorId, byte[] data) {
        this.userId = authorId;
        this.data = data;
    }
}
