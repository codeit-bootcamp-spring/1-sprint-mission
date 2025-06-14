package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(length = 100, nullable = false)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BinaryContentUploadStatus uploadStatus;

    public enum BinaryContentUploadStatus {
        WAITING,
        SUCCESS,
        FAILED
    }

    public static BinaryContent createBinaryContent(String fileName, Long size,
        String contentType) {
        return new BinaryContent(fileName, size, contentType, BinaryContentUploadStatus.WAITING);
    }

    private BinaryContent(String fileName, Long size, String contentType,
        BinaryContentUploadStatus uploadStatus) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.uploadStatus = uploadStatus;
    }

    public void updateUploadStatus(BinaryContentUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

}
