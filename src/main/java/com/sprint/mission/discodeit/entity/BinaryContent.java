package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable, Entity {

    private UUID id;
    private Instant createdAt;
    private UUID uploaderId;
    private BufferedImage image;
    private BinaryContentType type;

    //프로필사진저장하는데에도 이 클래스가 쓰인다고 가정하였음.
    public BinaryContent(UUID uploaderId, BinaryContentType binaryContentType, BufferedImage image) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.uploaderId = uploaderId;
        this.image = image;
        this.type = binaryContentType;
    }


}
