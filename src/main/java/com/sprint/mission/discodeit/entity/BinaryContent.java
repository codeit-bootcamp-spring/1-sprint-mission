package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID id;
    private Instant createAt;

    private String uploadFileName;

    public BinaryContent(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
}
