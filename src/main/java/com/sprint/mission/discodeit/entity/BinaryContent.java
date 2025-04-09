package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "binary_contents")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @OneToMany(mappedBy = "binaryContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MessageAttachment> messageAttachments = new ArrayList<>();
}