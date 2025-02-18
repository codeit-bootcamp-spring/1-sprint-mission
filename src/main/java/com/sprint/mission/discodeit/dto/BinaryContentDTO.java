package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.enums.ContentType;
import lombok.Builder;

import java.util.UUID;

public class BinaryContentDTO {

    @Builder
    public record request(byte[] file, UUID referenceId, String mimeType, String filename,
                          ContentType contentType) {
    }

    @Builder
    public record response(Long id, UUID uuid, byte[] file, UUID referenceId, ContentType contentType) {
    }

}
