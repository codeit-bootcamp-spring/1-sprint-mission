package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final UUID ownerId;
    private final String contentType;
    private final byte[] file;

    public BinaryContent(String fileName, UUID ownerId, String contentType, byte[] file){
        super();
        this.fileName = fileName;
        this.ownerId = ownerId;
        this.contentType = contentType;
        this.file = file;
    }

}
