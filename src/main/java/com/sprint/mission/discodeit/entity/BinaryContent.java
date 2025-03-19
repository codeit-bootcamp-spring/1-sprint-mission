package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
public class BinaryContent extends BaseUpdatableEntity {

    private String fileName;
    private Long size;
    private String contentType;

    @OneToOne(mappedBy = "profile")
    private User user;

    @OneToMany(mappedBy = "binaryContent", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MessageAttachment> messageAttachments = new ArrayList<>();

    public BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }
}