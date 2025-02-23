package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class BinaryContent extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final String contentType;
    private final Long size;
    private final byte[] file;

    public BinaryContent(String fileName, String contentType, byte[] file){
        super();
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
        this.size = (file != null) ? (long) file.length : 0;
    }
}
