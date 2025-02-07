package com.sprint.mission.discodeit.dto.binary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class BinaryContentCreateRequestDto {
    private UUID userId;
    private byte[] data;
    private UUID messageId;

    public BinaryContentCreateRequestDto(UUID authorId, byte[] data) {
        this.userId = authorId;
        this.data = data;
    }
}
