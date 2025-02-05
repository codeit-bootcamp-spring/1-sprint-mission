package com.sprint.mission.discodeit.entity.binarycontent;

import java.io.File;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;
    private final File file;

    public BinaryContent(File file) {
        Objects.requireNonNull(file, "file is null");
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.file = file;
    }

}
