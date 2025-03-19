package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * - 이미지, 파일 등 바이너리 데이터를 표현
 * - 사용자의 프로필 이미지/ 메시지 첨부 파일 저장을 위해 사용
 */
@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name", nullable = false)
    private String filename;

    @Column(name = "content_type", length = 50, nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long fileSize;

    @OneToMany(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageAttachment> messageAttachments = new ArrayList<>();


    public BinaryContent(String filename, String contentType, Long fileSize) {
        this.filename = filename;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}
