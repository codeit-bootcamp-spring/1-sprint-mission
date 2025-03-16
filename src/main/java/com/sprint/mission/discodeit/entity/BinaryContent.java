package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public boolean containsId(List<UUID> ids) {
        return ids.contains(this.getId());
    }
}
