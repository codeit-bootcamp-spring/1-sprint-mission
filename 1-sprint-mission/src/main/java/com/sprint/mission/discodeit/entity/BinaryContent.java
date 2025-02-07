package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private UUID id;
    private User user;
    private String filename;
    private byte[] fileData;

    public BinaryContent(User user, String filename, byte[] fileData) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.filename = filename;
        this.fileData = fileData;
    }
}
