package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentGetterDto(
        UUID id,
        Instant createdAt,
        User author,
        Channel channel,
        File content
) {
    public static BinaryContentGetterDto from (BinaryContent binaryContent){
        return new BinaryContentGetterDto(binaryContent.getId(), binaryContent.getCreatedAt(), binaryContent.getAuthor(), binaryContent.getChannel(), binaryContent.getContent());
    }
}
