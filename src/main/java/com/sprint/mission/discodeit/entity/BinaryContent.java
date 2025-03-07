package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "binary_contents")
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue
    @Column(name = "profile_id")
    private UUID id;

    private Instant createdAt;
    private String fileName;
    private Long size;
    private String contentType;
    @Lob
    private byte[] content;

    @OneToOne(mappedBy = "profile")
    private User user;

    @OneToMany(mappedBy = "binaryContent", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MessageAttachment> messageAttachments = new ArrayList<>();

    public BinaryContent(String fileName, Long size, String contentType, byte[] content) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.content = content;
    }
}