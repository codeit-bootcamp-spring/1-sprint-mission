package com.sprint.mission.discodeit.storage.s3;

import java.util.UUID;

public class BinaryContentDto {
    private final UUID id;
    private final String contentType;
    private final String filename;

    public BinaryContentDto(UUID id, String contentType, String filename) {
        this.id = id;
        this.contentType = contentType;
        this.filename = filename;
    }

    public UUID getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFilename() {
        return filename;
    }
}
